package com.rpm.rpmmovil.Routes//package com.rpm.rpmmovil.Rmotos
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Routes.apiRoute.PostRoutes
import com.rpm.rpmmovil.databinding.ActivitySaveRutasBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class saveRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveRutasBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var storage: FirebaseStorage
    private lateinit var token: String
    private var imageUri: Uri? = null

    //Metodo para tomar la url de la imagen de la galería del teléfono
    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri = uri
            Constains.ivImage.setImageURI(uri)
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        binding = ActivitySaveRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        storage = FirebaseStorage.getInstance()

        //Función inicio anónimo para acceder a firebase ??
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // El inicio de sesión anónimo fue exitoso
                    val user = task.result?.user
                    // Puedes usar el usuario actual para acceder a los datos protegidos por reglas de seguridad
                } else {
                    // Maneja el caso en el que el inicio de sesión anónimo falla
                    Log.e(
                        "saveRutasActivity",
                        "Error al iniciar sesión anónimamente",
                        task.exception
                    )
                }
            }

        //MÉTODO PARA SUBIR UNA FOTO DESDE LA GALERÍA DEL CELULAR
        Constains.ivImage = binding.imagenRuta
        binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        //Función para guardar la ruta
        binding.btnGuardarRoute.setOnClickListener {
            if (imageUri != null) {
                token = sharedPreferences.getString("token", "") ?: ""

                if (verificarToken()) {
                    subirImagen()
                } else {
                    Toast.makeText(
                        this@saveRutasActivity,
                        "Usuario no autenticado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Función para subir imagen a firebase
    private fun subirImagen() {
        if (imageUri != null) {
            val storageRef = storage.reference
            val imageRef = storageRef.child("imagenes/imagen_${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { uploadTask ->
                    uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d("saveRutasActivity", "Enlace de descarga de la imagen: $downloadUrl")
                        Toast.makeText(
                            this@saveRutasActivity,
                            "Imagen subida con éxito",
                            Toast.LENGTH_SHORT
                        ).show()

                        guardarRutaDb(downloadUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@saveRutasActivity,
                        "Error al subir la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(
                this@saveRutasActivity,
                "La URI de la imagen es nula",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Función para enviar datos a base datos API
    private fun guardarRutaDb(imageUrl: String) {
        val nombreRuta = binding.nombreRuta.text.toString()

        val cordenadasInicioOriginal = intent.extras?.getString("cordenadasInicio").orEmpty()
        val cordenadasFinalOriginal = intent.extras?.getString("cordenadasFinal").orEmpty()

        // Cambiar el formato de las coordenadas
        val cordenadasInicio = formatearCoordenadas(cordenadasInicioOriginal)
        val cordenadasFinal = formatearCoordenadas(cordenadasFinalOriginal)

        val kmRuta = Constains.DISTANCIA_RUTA
        val ppto = 5000
        val imagenRuta = imageUrl
        val detalleRuta = binding.detallesRuta.text.toString()
        val calificacion = binding.ratingBar.rating.toInt()
        val motoviajero = ""

        val route = PostRoutes(
            nombreRuta,
            cordenadasInicio,
            cordenadasFinal,
            kmRuta,
            ppto,
            imagenRuta,
            detalleRuta,
            calificacion,
            motoviajero
        )

        //Función para validar el token
        token = sharedPreferences.getString("token", "") ?: ""

        if (verificarToken()) {
            //val retro = ApiClient.web
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rpm-back-end.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(
                            15,
                            TimeUnit.SECONDS
                        ) // Ajusta el tiempo de espera según tus necesidades
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .build()
                )
                .build()

            //Corrutine para enviar datos a API por retrofit
            val service = retrofit.create(ApiServices::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = service.PostSaveRoutes(token, route)
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                this@saveRutasActivity,
                                "Ruta guardada exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@saveRutasActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("saveRutasActivity", "Error en la respuesta: $errorBody")
                        runOnUiThread {
                            Toast.makeText(
                                this@saveRutasActivity,
                                "Error al guardar los datos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("saveRutasActivity", "Error al guardar los datos: ${e.message}", e)
                    runOnUiThread {
                        Toast.makeText(
                            this@saveRutasActivity,
                            "Error al guardar los datos: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(this@saveRutasActivity, "Usuario no autenticado", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun verificarToken(): Boolean {
        val token: String? = sharedPreferences.getString("token", null)
        Log.d("TOKEN", "$token")
        return !token.isNullOrBlank()
    }


    private fun formatearCoordenadas(coordenadas: String): String {
        val coordenadasRegex = Regex("lat/lng: \\((-?\\d+\\.\\d+), (-?\\d+\\.\\d+)\\)")
        val matchResult = coordenadasRegex.find(coordenadas)

        return matchResult?.let { match ->
            val (latitud, longitud) = match.destructured
            "$latitud, $longitud"
        } ?: coordenadas.replace("lat/lng: (", "").replace(")", "")
    }

}