package com.rpm.rpmmovil.Rmotos//package com.rpm.rpmmovil.Rmotos


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.Rmotos.model.Apis.ApiServiceMotos
import com.rpm.rpmmovil.Rmotos.model.Apis.Data.DataItemMotos
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GarageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var storage: FirebaseStorage
    private lateinit var token: String
    private var imageUri: Uri? = null
    private var selectedImageByte: ByteArray? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            val result = bitmapToByteArray(bitmapImage)
            selectedImageByte = result
            binding.imageView.setImageBitmap(bitmapImage)

            val aspectRatio = bitmapImage.width.toFloat() / bitmapImage.height.toFloat()
            val newHeight = (binding.imageView.width / aspectRatio).toInt()
            binding.imageView.layoutParams.height = newHeight
            binding.imageView.requestLayout()
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        storage = FirebaseStorage.getInstance()

        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // El inicio de sesión anónimo fue exitoso
                    val user = task.result?.user
                    // Puedes usar el usuario actual para acceder a los datos protegidos por reglas de seguridad
                } else {
                    // Maneja el caso en el que el inicio de sesión anónimo falla
                    Log.e("GarageActivity", "Error al iniciar sesión anónimamente", task.exception)
                }
            }

        binding.garage.setOnClickListener {
            val intent = Intent(this, ShowGarageActivity::class.java)
            startActivity(intent)
        }

        binding.imageButton.setOnClickListener {
            pickMedia.launch("image/*")
        }

        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val nombre = binding.motonombre.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumo.text.toString()

            if (selectedImageByte != null) {
                val motoRegisterData = DataItemMotos(
                    motonom = nombre,
                    motomarca = marca,
                    motomodel = modelo,
                    motovers = version.toIntOrNull() ?: 0,
                    consumokmxg = consumo.toIntOrNull() ?: 0,
                    cilimoto = cilindraje,
                    imagemoto = imageUri.toString()
                )

                val imageFilePart = selectedImageByte?.let {
                    MultipartBody.Part.createFormData(
                        "image",
                        "image.jpg",
                        it.toRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                token = sharedPreferences.getString("token", "") ?: ""

                if (verificarToken()) {
                    subirImagen(imageFilePart)
                } else {
                    Toast.makeText(this@GarageActivity, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun subirImagen(imageFile: MultipartBody.Part?) {
        if (imageFile != null && imageUri != null) {
            val storageRef = storage.reference
            val imageRef = storageRef.child("imagenes/imagen_${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { uploadTask ->
                    uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d("GarageActivity", "Enlace de descarga de la imagen: $downloadUrl")
                        Toast.makeText(this@GarageActivity, "Imagen subida con éxito", Toast.LENGTH_SHORT).show()

                        guardarDatosEnBaseDeDatos(downloadUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@GarageActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@GarageActivity, "La URI de la imagen es nula", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarDatosEnBaseDeDatos(imageUrl: String) {
        val marca = binding.marcamoto.text.toString()
        val modelo = binding.modelomoto.text.toString()
        val cilindraje = binding.cilindrajemoto.text.toString()
        val nombre = binding.motonombre.text.toString()
        val version = binding.versionmoto.text.toString()
        val consumo = binding.consumo.text.toString()

        val motoRegisterData = DataItemMotos(
            motonom = nombre,
            motomarca = marca,
            motomodel = modelo,
            motovers = version.toIntOrNull() ?: 0,
            consumokmxg = consumo.toIntOrNull() ?: 0,
            cilimoto = cilindraje,
            imagemoto = imageUrl
        )

        token = sharedPreferences.getString("token", "") ?: ""

        if (verificarToken()) {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rpm-back-end.vercel.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS) // Ajusta el tiempo de espera según tus necesidades
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .build()
                )
                .build()


            val service = retrofit.create(ApiServiceMotos::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = service.postDataMoto(token, motoRegisterData)
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@GarageActivity, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@GarageActivity, ShowGarageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("GarageActivity", "Error en la respuesta: $errorBody")
                        runOnUiThread {
                            Toast.makeText(this@GarageActivity, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("GarageActivity", "Error al guardar los datos: ${e.message}", e)
                    runOnUiThread {
                        Toast.makeText(this@GarageActivity, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this@GarageActivity, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verificarToken(): Boolean {
        val token: String? = sharedPreferences.getString("token", null)
        Log.d("TOKEN", "$token")
        return !token.isNullOrBlank()
    }


}
