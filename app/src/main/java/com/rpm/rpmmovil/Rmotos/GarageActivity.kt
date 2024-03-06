//package com.rpm.rpmmovil.Rmotos
//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
//import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
//import com.rpm.rpmmovil.interfaces.ApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.ByteArrayOutputStream
//
//class GarageActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityGarajeBinding
//    private lateinit var sharedPreferences: SharedPreferences
//    private var selectedImageByte: ByteArray? = null
//
//    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//        if (uri != null) {
//            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
//
//            val result = bitmapToByteArray(bitmapImage)
//            selectedImageByte = result
//
//            binding.imageView.setImageBitmap(bitmapImage)
//        } else {
//            // No se seleccionó ninguna imagen
//            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityGarajeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//
//
//
//        binding.garage.setOnClickListener {
//            val intent = Intent(this, ShowGarageActivity::class.java)
//            startActivity(intent)
//        }
//
//        val imgmoto = binding.imageButton.setOnClickListener {
//            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        }
//
//        // Registrar la selección de imágenes
//
//        // Configurar el evento click del botón de registro
//        binding.register.setOnClickListener {
//            val marca = binding.marcamoto.text.toString()
//            val modelo = binding.modelomoto.text.toString()
//            val cilindraje = binding.cilindrajemoto.text.toString()
//            val placa = binding.placamoto.text.toString()
//            val nombre = binding.motonombre.text.toString()
//            val version = binding.versionmoto.text.toString()
//            val consumo = binding.consumo.text.toString()
////            val imagenmoto = imgmoto.toString()
//
//            // Validar campos obligatorios
//            if (marca.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
//                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
//            } else {
//                // Crear un objeto DataItemMotos con la información de la motocicleta
//                val motoRegisterData = DataItemMotos(
//                    motonom = nombre,
//                    motomarca = marca,
//                    motomodel = modelo,
//                    motovers = version.toIntOrNull() ?: 0,
//                    consumokmxg = consumo.toIntOrNull() ?: 0,
//                    cilimoto = cilindraje,
//                    imagemoto = imgmoto.toString()
//                )
//                val token = sharedPreferences.getString("token", null)
//                val call: Call<DataItemMotos> = ApiClient.web.PostRegisterMoto(motoRegisterData,token!!)
//
//                call.enqueue(object : Callback<DataItemMotos> {
//                    override fun onResponse(
//                        call: Call<DataItemMotos>,
//                        response: Response<DataItemMotos>
//                    ) {
//                        if (response.isSuccessful) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Registro exitoso",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Toast.makeText(
//                                applicationContext,
//                                "Error en el registro",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                        }
//                    }
//
//                    override fun onFailure(call: Call<DataItemMotos>, t: Throwable) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Error en el registro",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                })
//
//
//            }
//        }
//    }
//
//    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        return stream.toByteArray()
//    }
//}
//
////    private fun registerNewMot(motoRegisterData: DataItemMotos) {
////        lifecycleScope.launch {
////            try {
////                val token = sharedPreferences.getString("token", null)
////                val result = ApiClient.web.PostRegisterMoto(motoRegisterData, token!!)
////                Log.e("TAG", "${result}", )
////            }catch (e:Exception){
////                Log.e("TAG", "${e}", )
////                Toast.makeText(this@GarageActivity, "Ocurrio un error", Toast.LENGTH_SHORT).show()
////            }
////        }
////    }
////}
//
//

//
//package com.rpm.rpmmovil.Rmotos
//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
//import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
//import com.rpm.rpmmovil.interfaces.ApiClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.ByteArrayOutputStream
//
//class GarageActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityGarajeBinding
//    private lateinit var sharedPreferences: SharedPreferences
//    private var selectedImageByte: ByteArray? = null
//
//    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//        if (uri != null) {
//            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
//
//            val result = bitmapToByteArray(bitmapImage)
//            selectedImageByte = result
//
//            binding.imageView.setImageBitmap(bitmapImage)
//        } else {
//            // No se seleccionó ninguna imagen
//            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityGarajeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
//
//        binding.garage.setOnClickListener {
//            val intent = Intent(this, ShowGarageActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.imageButton.setOnClickListener {
//            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        }
//
//        // Configurar el evento click del botón de registro
//        binding.register.setOnClickListener {
//            val marca = binding.marcamoto.text.toString()
//            val modelo = binding.modelomoto.text.toString()
//            val cilindraje = binding.cilindrajemoto.text.toString()
//            val placa = binding.placamoto.text.toString()
//            val nombre = binding.motonombre.text.toString()
//            val version = binding.versionmoto.text.toString()
//            val consumo = binding.consumo.text.toString()
//
//            // Validar campos obligatorios
//            if (marca.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
//                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
//            } else {
//                // Verificar si la imagen ha sido seleccionada antes de continuar
//                if (selectedImageByte != null) {
//                    // Crear un objeto DataItemMotos con la información de la motocicleta
//                    val motoRegisterData = DataItemMotos(
//                        motonom = nombre,
//                        motomarca = marca,
//                        motomodel = modelo,
//                        motovers = version.toIntOrNull() ?: 0,
//                        consumokmxg = consumo.toIntOrNull() ?: 0,
//                        cilimoto = cilindraje,
//                        imagemoto = selectedImageByte.toString()
//                    )
//
//                    val token = sharedPreferences.getString("token", null)
//                    val call: Call<DataItemMotos> =
//                        ApiClient.web.PostRegisterMoto(motoRegisterData, token!!)
//
//                    call.enqueue(object : Callback<DataItemMotos> {
//                        override fun onResponse(
//                            call: Call<DataItemMotos>,
//                            response: Response<DataItemMotos>
//                        ) {
//                            if (response.isSuccessful) {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Registro exitoso",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Error en el registro",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<DataItemMotos>, t: Throwable) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Error en el registro",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    })
//                } else {
//                    Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        return stream.toByteArray()
//    }
//}

package com.rpm.rpmmovil.Rmotos

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class GarageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImageByte: ByteArray? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

            val result = bitmapToByteArray(bitmapImage)
            selectedImageByte = result

            binding.imageView.setImageBitmap(bitmapImage)
        } else {
            // No se seleccionó ninguna imagen
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        storage = Firebase.storage

        binding.garage.setOnClickListener {
            val intent = Intent(this, ShowGarageActivity::class.java)
            startActivity(intent)
        }

        binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val placa = binding.placamoto.text.toString()
            val nombre = binding.motonombre.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumo.text.toString()
            val imgmoto = String

            // Validar campos obligatorios
            if (marca.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar si la imagen ha sido seleccionada antes de continuar
                if (selectedImageByte != null) {
                    // Crear un objeto DataItemMotos con la información de la motocicleta
                    val motoRegisterData = DataItemMotos(
                        motonom = nombre,
                        motomarca = marca,
                        motomodel = modelo,
                        motovers = version.toIntOrNull() ?: 0,
                        consumokmxg = consumo.toIntOrNull() ?: 0,
                        cilimoto = cilindraje,
                        imagemoto = selectedImageByte.toString()
                    )

                    // Crear un objeto MultipartBody.Part para la imagen
                    val imageFilePart = selectedImageByte?.let {
                        MultipartBody.Part.createFormData(
                            "image",
                            "image.jpg",
                            it.toRequestBody("image/*".toMediaTypeOrNull())
                        )
                    }

                    registerNewMot(motoRegisterData, imageFilePart)
                } else {
                    Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun registerNewMot(motoRegisterData: DataItemMotos, imageFile: MultipartBody.Part?) {
        lifecycleScope.launch {
            try {
                val token = sharedPreferences.getString("token", null)

                // Verificar la nulidad de token e imageFile antes de hacer la solicitud
                if (token != null && imageFile != null) {
                    // Agregar el archivo de imagen como parte del cuerpo de la solicitud
                    val call =
                        ApiClient.web.PostRegisterMotoWithImage(motoRegisterData, imageFile, token)


                    // Utilizar el método enqueue para manejar la respuesta asíncronamente
                    call.enqueue(object : Callback<DataItemMotos> {
                        override fun onResponse(
                            call: Call<DataItemMotos>,
                            response: Response<DataItemMotos>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("TAG", "Registro exitoso: ${response.body()}")
                                Toast.makeText(
                                    applicationContext,
                                    "Registro exitoso",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.e(
                                    "TAG",
                                    "Error en el registro: ${response.code()} - ${response.message()}"
                                )
                                Toast.makeText(
                                    applicationContext,
                                    "Error en el registro",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DataItemMotos>, t: Throwable) {
                            Log.e("TAG", "Error en el registro: $t")
                            Toast.makeText(
                                applicationContext,
                                "Error en el registro",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                } else {
                    Log.e("TAG", "Token o imageFile es nulo")
                    Toast.makeText(this@GarageActivity, "Ocurrió un error", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e("TAG", "Excepción: $e")
                Toast.makeText(this@GarageActivity, "Ocurrió un error", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun seleccionarImagen(){
        val intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }
    private fun subirImagen(){
        if (imageUri != null){
            val storageRef = storage.reference
            val imageRef = storageRef.child("imagenes/imagen_"+System.currentTimeMillis()+".jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                 it.metadata?.reference?.downloadUrl
                }
                .addOnFailureListener {
                    Toast.makeText(this@GarageActivity,"Error al subir la imagen",Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PICK_IMAGE_REQUEST && resultCode ==Activity.RESULT_OK && data != null && data.data != null){
            imageUri = data.data
        }
    }
}

