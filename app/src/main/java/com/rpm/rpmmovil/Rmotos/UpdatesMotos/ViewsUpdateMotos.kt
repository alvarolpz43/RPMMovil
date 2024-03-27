package com.rpm.rpmmovil.Rmotos.UpdatesMotos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.updateMoto
import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.utils.AppRPM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


import com.squareup.picasso.Picasso

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ViewsUpdateMotos : AppCompatActivity() {
    private lateinit var binding: ActivityViewsUpdateMotosBinding
    val token = AppRPM.prefe.getToken().toString()
    val retrofit = ApiClient.web

    //lo de firebase
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private var imageUri: Uri? = null
    //pick

    private var selectedImageByte: ByteArray? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            val result = bitmapToByteArray(bitmapImage)
            selectedImageByte = result
            binding.imagemoto.setImageBitmap(bitmapImage)
            Log.i("sube", selectedImageByte.toString())
        } else {
            // El usuario no seleccionó ninguna imagen nueva, mantener la imagen actual
            val currentImageDrawable = binding.imagemoto.drawable
            selectedImageByte =
                if (currentImageDrawable != null && currentImageDrawable is BitmapDrawable) {
                    val currentBitmap = currentImageDrawable.bitmap
                    bitmapToByteArray(currentBitmap)
                } else {
                    null // Opcionalmente, puedes asignar otro valor en lugar de null
                }

            Log.i("cargada", "${selectedImageByte}")
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewsUpdateMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Storage
        FirebaseApp.initializeApp(this)
        storage = FirebaseStorage.getInstance()
        // Set OnClickListener to select image from gallery


        binding.imagemoto.setOnClickListener {
            pickMedia.launch("image/*")
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


        //para usarlo en el edit
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

        //inputs
        val editTexts = listOf(
            binding.motomodelo,
            binding.imagemoto,
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
            val imageFilePart = selectedImageByte?.let {
                MultipartBody.Part.createFormData(
                    "image",
                    "image.jpg",
                    it.toRequestBody("image/*".toMediaTypeOrNull())
                )
            }
            if (imageFilePart != null) {

                subirImagen(imageFilePart)


            } else {
                // No se seleccionó una nueva imagen, actualizar otros datos sin cambiar la imagen

            }
        }
    }

    private fun updateInfo(imgUsuario: String) {
        // Para usarlo en la edición
        val idUserMoto = intent.getStringExtra("idMoto")

        val nombre = binding.motonombre.text.toString()
        val modelo = binding.motomodelo.text.toString()
        val marca = binding.marcamoto.text.toString()
        val version = binding.versionmoto.text.toString()
        val consumo = binding.consumokmxg.text.toString()
        val cilindraje = binding.cilimoto.text.toString()

        val updatedMoto = updateMoto(
            nombre,
            marca,
            modelo,
            version.toInt(),
            consumo.toInt(),
            cilindraje,
            imgUsuario

        )


        lifecycleScope.launch(Dispatchers.IO) {

            try {
                val response = retrofit.updateMoto(idUserMoto.toString(), updatedMoto, token)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@ViewsUpdateMotos, ShowGarageActivity::class.java))
                    }
                } else {
                    Log.e("Fallo", "Por mk")
                    showToast("Failed to update moto")
                }
            } catch (e: Exception) {
                Log.e("UpdateMoto", "Error: ${e.message}")
                showToast("Failed to update moto")
            }
        }
    }
    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun subirImagen(imageFile: MultipartBody.Part?) {
        if (imageFile != null) {
            val storageRef = storage.reference
            val imageRef = storageRef.child("imagenes/imagen_${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { uploadTask ->
                    uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d("GarageActivity", "Enlace de descarga de la imagen: $downloadUrl")
                        Toast.makeText(
                            this@ViewsUpdateMotos,
                            "Imagen subida con éxito",
                            Toast.LENGTH_SHORT
                        ).show()

                        updateInfo(downloadUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@ViewsUpdateMotos,
                        "Error al subir la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this@ViewsUpdateMotos, "La URI de la imagen es nula", Toast.LENGTH_SHORT)
                .show()


        }
    }

}



