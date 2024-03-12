//package com.rpm.rpmmovil.Routes
//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.storage.FirebaseStorage
//import com.rpm.rpmmovil.MainActivity
//import com.rpm.rpmmovil.Model.Constains
//import com.rpm.rpmmovil.Routes.apiRoute.PostRoutes
//import com.rpm.rpmmovil.databinding.ActivitySaveRutasBinding
//import com.rpm.rpmmovil.interfaces.ApiClient
//import kotlinx.coroutines.launch
//import java.util.UUID
//
//class saveRutasActivity : AppCompatActivity() {
//    private lateinit var binding: ActivitySaveRutasBinding
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    private lateinit var storage: FirebaseStorage
//    private lateinit var token: String
//    private var imageUri: Uri? = null
//    private var selectedImageByte: ByteArray? = null
//
//    var urlImagenRuta = ""
//
//    // Variable para subir imágen a firebase //...
//    private val PICK_IMAGE_REQUEST_CODE = 123
//
//    //Funcion para tomar la dirección de la imagen que se sube de la galería e insertarla
//    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//        if (uri != null) {
//            // Imagen seleccionada
//            Constains.URI.setImageURI(uri)
//        } else {
//            Toast.makeText(this, "No se seleccionó ninguna imágen", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivitySaveRutasBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        //GUARDA LAS CORDENADAS DE LA RUTA
//        val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
//        val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()
//        binding.cordenadasRutaInicio.text = cordenadasInicio
//        binding.cordenadasRutaFinal.text = cordenadasFinal
//
//        // Se traen las preferencias del usuario para verificar si está logeado
//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//        storage = FirebaseStorage.getInstance()
//
//        // Función para saber si el usuario está logeado
//        FirebaseAuth.getInstance().signInAnonymously()
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // El inicio de sesión anónimo fue exitoso
//                    val user = task.result?.user
//                    // Puedes usar el usuario actual para acceder a los datos protegidos por reglas de seguridad
//                } else {
//                    // Maneja el caso en el que el inicio de sesión anónimo falla
//                    Log.e("GarageActivity", "Error al iniciar sesión anónimamente", task.exception)
//                }
//            }
//
//        //MÉTODO PARA SUBIR UNA FOTO DESDE LA GALERÍA DEL CELULAR
//        Constains.URI = binding.imagenRuta
//        binding.imageButton.setOnClickListener {
//            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        }
//
//        //BOTON GUARDAR RUTA
//        binding.btnGuardarRoute.setOnClickListener {
//            //Función de guardar la ruta
//            guardarRuta()
//
//            //Aquí iría la función guardar imagen en firebase
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//
//    }
//
//    fun guardarRuta() {
//
//        val nombreRuta = binding.nombreRuta.text.toString()
//        val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
//        val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()
//        val kmRuta = Constains.DISTANCIA_RUTA
//        val ppto = 5000
//        val imagenRuta = urlImagenRuta
//        val detalleRuta = binding.detallesRuta.text.toString()
//        val calificacion = 5
//        val motoviajero = "fasfdf5456464"
//
//        val route = PostRoutes(
//            nombreRuta,
//            cordenadasInicio,
//            cordenadasFinal,
//            kmRuta,
//            ppto,
//            imagenRuta,
//            detalleRuta,
//            calificacion,
//            motoviajero
//        )
//
//        token = sharedPreferences.getString("token", "") ?: ""
//
////        if (verificarToken()) {
////            subirImagen(imageFilePart)
////        } else {
////            Toast.makeText(
////                this@GarageActivity,
////                "Usuario no autenticado",
////                Toast.LENGTH_SHORT
////            ).show()
////        }
//
//        saveNewRoute(route)
//
//    }
//
//    private fun obtenerToken(): String {
//        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//        return sharedPreferences.getString("token", "") ?: ""
//    }
//
//    private fun saveNewRoute(postRoute: PostRoutes) {
//        lifecycleScope.launch {
//
//            val token = obtenerToken()
//            try {
//                val result = ApiClient.web.PostSaveRoutes(postRoute, token!!)
//                Log.e("TAG", "${result}")
//            } catch (e: Exception) {
//                Log.e("TAG", "${e}")
//                Toast.makeText(this@saveRutasActivity, "Ocurrio un error", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
//
//    // GUARDAR IMAGEN EN FIREBASE
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
//            val uri = data?.data
//
//            // Subir imagen a Firebase Storage
//            val storageRef = FirebaseStorage.getInstance().reference
//            val filePath = "images/${UUID.randomUUID()}.jpg"
//            val imageRef = storageRef.child(filePath)
//
//            val uploadTask = imageRef.putFile(uri!!)
//
//            uploadTask.addOnSuccessListener {
//                // Imagen subida exitosamente
//                // val downloadUrl = it.metadata?.downloadUrl
//                //Mostrar la URL en un TextView
//                // urlImagenRuta = downloadUrl.toString()
//            }
//
//            uploadTask.addOnFailureListener {
//                // Error al subir la imagen
//                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
//            }
//        }
//    } //Aqui termina codigo para guardar imagen en firebase
//
//    // Función para verificar el token
//    private fun verificarToken(): Boolean {
//        val token: String? = sharedPreferences.getString("token", null)
//        Log.d("TOKEN", "$token")
//        return !token.isNullOrBlank()
//    }
//
//}
