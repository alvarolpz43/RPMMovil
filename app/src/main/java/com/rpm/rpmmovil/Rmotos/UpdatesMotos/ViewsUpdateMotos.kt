//package com.rpm.rpmmovil.Rmotos.UpdatesMotos
//
//import DataItemMotos
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import com.rpm.rpmmovil.R
//import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
//import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.updateMoto
//import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
//import com.rpm.rpmmovil.interfaces.ApiClient
//import com.rpm.rpmmovil.utils.AppRPM
//import com.squareup.picasso.Picasso
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class ViewsUpdateMotos : AppCompatActivity() {
//    private lateinit var binding: ActivityViewsUpdateMotosBinding
//    private var selectedImageUri: Uri? = null
//    private val token = AppRPM.prefe.getToken().toString()
//    private val retrofit = ApiClient.web
//    private lateinit var storageReference: StorageReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityViewsUpdateMotosBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Initialize Firebase Storage
//        storageReference = FirebaseStorage.getInstance().reference
//        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d("ViewsUpdateMotos", "Firebase authentication successful")
//            } else {
//                Log.e("ViewsUpdateMotos", "Firebase authentication failed: ${task.exception}")
//            }
//        }
//        // Set OnClickListener to select image from gallery
//        binding.imagemoto.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
//        }
//
//        // Ocultar el botón "Subir Imagen" inicialmente
//
//
//        getUserMotosId()
//    }
//
//    private fun getUserMotosId() {
//        val motonom = intent.getStringExtra("motonom")
//        val motomodel = intent.getStringExtra("motomodel")
//        val motomarca = intent.getStringExtra("motomarca")
//        val motovers = intent.getStringExtra("motovers")
//        val consumokmxg = intent.getStringExtra("consumokmxg")
//        val cilimoto = intent.getStringExtra("cilimoto")
//
//        // Para usarlo en la edición
//        val idUserMoto = intent.getStringExtra("idMoto")
//
//        binding.motonombre.setText(motonom)
//        binding.motomodelo.setText(motomodel)
//        binding.marcamoto.setText(motomarca)
//        binding.versionmoto.setText(motovers)
//        binding.consumokmxg.setText(consumokmxg)
//        binding.cilimoto.setText(cilimoto)
//
//        // Obtener la URL de la imagen del intent
//        val imagemoto = intent.getStringExtra("imagemoto")
//        if (!imagemoto.isNullOrEmpty()) {
//            Picasso.get().load(imagemoto).into(binding.imagemoto)
//        }
//
//        // Inputs
//        val editTexts = listOf(
//            binding.motomodelo,
//            binding.marcamoto,
//            binding.motonombre,
//            binding.marcamoto,
//            binding.cilimoto,
//            binding.versionmoto,
//            binding.consumokmxg
//        )
//        editTexts.forEach { it.isEnabled = false }
//        binding.btnEdit.setOnClickListener {
//            editTexts.forEach { it.isEnabled = true }
//            // Mostrar el botón "Subir Imagen" cuando se hace clic en "Editar"
//
//        }
//
//        binding.btnGuardar.setOnClickListener {
//            val nombre = binding.motonombre.text.toString()
//            val modelo = binding.motomodelo.text.toString()
//            val marca = binding.marcamoto.text.toString()
//            val version = binding.versionmoto.text.toString()
//            val consumo = binding.consumokmxg.text.toString()
//            val cilindraje = binding.cilimoto.text.toString()
//
//            if (selectedImageUri != null && FirebaseAuth.getInstance().currentUser != null) {
//                uploadImageToFirebaseStorage(
//                    nombre,
//                    modelo,
//                    marca,
//                    version,
//                    consumo,
//                    cilindraje,
//                    idUserMoto
//                )
//            } else {
//                saveDataToServer(
//                    nombre,
//                    modelo,
//                    marca,
//                    version,
//                    consumo,
//                    cilindraje,
//                    idUserMoto,
//                    ""
//                )
//            }
//        }
//    }
//
//    // Handle result of image selection from gallery
//    // Handle result of image selection from gallery
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            selectedImageUri = data.data
//            // Cargar la imagen utilizando Picasso
//            Picasso.get().load(selectedImageUri).into(binding.imagemoto)
//        }
//    }
//
//
//    private fun uploadImageToFirebaseStorage(
//        nombre: String,
//        modelo: String,
//        marca: String,
//        version: String,
//        consumo: String,
//        cilindraje: String,
//        idUserMoto: String?
//    ) {
//        val imageRef = storageReference.child("imagenes/${System.currentTimeMillis()}.jpg")
//        val uploadTask = imageRef.putFile(selectedImageUri!!)
//
//        uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let {
//                    throw it
//                }
//            }
//            imageRef.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result
//                Log.d("ImageURL", "Image uploaded successfully. URL: $downloadUri")
//                saveDataToServer(
//                    nombre,
//                    modelo,
//                    marca,
//                    version,
//                    consumo,
//                    cilindraje,
//                    idUserMoto,
//                    downloadUri.toString()
//                )
//            } else {
//                Log.e("UploadImage", "Failed to upload image")
//                showToast("Failed to upload image")
//            }
//        }
//    }
//
//    private fun saveDataToServer(
//        nombre: String,
//        modelo: String,
//        marca: String,
//        version: String,
//        consumo: String,
//        cilindraje: String,
//        idUserMoto: String?,
//        imageUrl: String
//    ) {
//        if (selectedImageUri != null && idUserMoto != null) {
//            val imageRef = storageReference.child("imagenes/${System.currentTimeMillis()}.jpg")
//            val uploadTask = imageRef.putFile(selectedImageUri!!)
//
//            uploadTask.continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                imageRef.downloadUrl
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val downloadUri = task.result
//                    // Crear un objeto updateMoto con los datos actualizados, incluida la nueva URL de la imagen
//                    val updatedMoto = updateMoto(
//                        nombre,
//                        marca,
//                        modelo,
//                        version.toInt(),
//                        consumo.toInt(),
//                        cilindraje,
//                        downloadUri.toString() // Nueva URL de la imagen
//                    )
//
//                    // Enviar la solicitud de actualización al servidor
//                    lifecycleScope.launch(Dispatchers.IO) {
//                        try {
//                            val response = retrofit.updateMoto(idUserMoto, updatedMoto, token)
//                            if (response.isSuccessful) {
//                                withContext(Dispatchers.Main) {
//                                    startActivity(Intent(this@ViewsUpdateMotos, ShowGarageActivity::class.java))
//                                }
//                            } else {
//                                Log.e("UpdateMoto", "Failed to update moto")
//                                showToast("Failed to update moto")
//                            }
//                        } catch (e: Exception) {
//                            Log.e("UpdateMoto", "Error: ${e.message}")
//                            showToast("Failed to update moto")
//                        }
//                    }
//                } else {
//                    Log.e("UploadImage", "Failed to upload image")
//                    showToast("Failed to upload image")
//                }
//            }
//        } else {
//            Log.e("saveDataToServer", "selectedImageUri or idUserMoto is null")
//        }
//    }
//
//
//
//
//
//    private fun showToast(message: String) {
//        runOnUiThread {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    companion object {
//        private const val IMAGE_PICK_REQUEST_CODE = 100
//    }
//}



package com.rpm.rpmmovil.Rmotos.UpdatesMotos

import DataItemMotos
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.updateMoto
import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

@Suppress("DEPRECATION")
class ViewsUpdateMotos : AppCompatActivity() {
    private lateinit var binding: ActivityViewsUpdateMotosBinding
    private var selectedImageUri: Uri? = null
    private val token = AppRPM.prefe.getToken().toString()
    private val retrofit = ApiClient.web
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewsUpdateMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("ViewsUpdateMotos", "Firebase authentication successful")
            } else {
                Log.e("ViewsUpdateMotos", "Firebase authentication failed: ${task.exception}")
            }
        }
        // Set OnClickListener to select image from gallery
        binding.imagemoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
        }

        getUserMotosId()
    }

    private fun getUserMotosId() {
        val motonom = intent.getStringExtra("motonom")
        val motomodel = intent.getStringExtra("motomodel")
        val motomarca = intent.getStringExtra("motomarca")
        val motovers = intent.getStringExtra("motovers")
        val consumokmxg = intent.getStringExtra("consumokmxg")
        val cilimoto = intent.getStringExtra("cilimoto")

        // Para usarlo en la edición
        val idUserMoto = intent.getStringExtra("idMoto")

        binding.motonombre.setText(motonom)
        binding.motomodelo.setText(motomodel)
        binding.marcamoto.setText(motomarca)
        binding.versionmoto.setText(motovers)
        binding.consumokmxg.setText(consumokmxg)
        binding.cilimoto.setText(cilimoto)

        // Obtener la URL de la imagen del intent
        val imagemoto = intent.getStringExtra("imagemoto")
        if (!imagemoto.isNullOrEmpty()) {
            Picasso.get().load(imagemoto).into(binding.imagemoto)
        }

        // Inputs
        val editTexts = listOf(
            binding.motomodelo,
            binding.marcamoto,
            binding.motonombre,
            binding.marcamoto,
            binding.cilimoto,
            binding.versionmoto,
            binding.consumokmxg
        )
        editTexts.forEach { it.isEnabled = false }
        binding.btnEdit.setOnClickListener {
            editTexts.forEach { it.isEnabled = true }
        }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.motonombre.text.toString()
            val modelo = binding.motomodelo.text.toString()
            val marca = binding.marcamoto.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumokmxg.text.toString()
            val cilindraje = binding.cilimoto.text.toString()

            if (selectedImageUri != null && FirebaseAuth.getInstance().currentUser != null) {
                subirImagen(nombre, modelo, marca, version, consumo, cilindraje, idUserMoto)
            } else {
                saveDataToServer(nombre, modelo, marca, version, consumo, cilindraje, idUserMoto, "")
            }
        }
    }

    private fun subirImagen(
        nombre: String,
        modelo: String,
        marca: String,
        version: String,
        consumo: String,
        cilindraje: String,
        idUserMoto: String?
    ) {
        val storageRef = storageReference.child("imagenes/imagen_${System.currentTimeMillis()}.jpg")
        storageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Log.d("ViewsUpdateMotos", "Enlace de descarga de la imagen: $downloadUrl")
                    Toast.makeText(this@ViewsUpdateMotos, "Imagen subida con éxito", Toast.LENGTH_SHORT).show()

                    saveDataToServer(nombre, modelo, marca, version, consumo, cilindraje, idUserMoto, downloadUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@ViewsUpdateMotos, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToServer(
        nombre: String,
        modelo: String,
        marca: String,
        version: String,
        consumo: String,
        cilindraje: String,
        idUserMoto: String?,
        imageUrl: String
    ) {
        val updatedMoto = updateMoto(
            nombre,
            marca,
            modelo,
            version.toInt(),
            consumo.toInt(),
            cilindraje,
            imageUrl
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = retrofit.updateMoto(idUserMoto.toString(), updatedMoto, token)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@ViewsUpdateMotos, ShowGarageActivity::class.java))
                    }
                } else {
                    Log.e("UpdateMoto", "Failed to update moto")
                    showToast("Failed to update moto")
                }
            } catch (e: Exception) {
                Log.e("UpdateMoto", "Error: ${e.message}")
                showToast("Failed to update moto")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 100
    }
}
