package com.rpm.rpmmovil.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ViewProfile : AppCompatActivity() {
    private lateinit var binding: ActivityViewProfileBinding
    private lateinit var storage: FirebaseStorage
    val token = AppRPM.prefe.getToken().toString()
    private var imageUri: Uri? = null
    var idUserFoto: String? =null


    //pick
    private var selectedImageByte: ByteArray? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { // Verificar si se seleccionó una URI de imagen
            imageUri = it
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
            selectedImageByte = bitmapToByteArray(bitmapImage)
            binding.userPhoto.setImageBitmap(bitmapImage)
            Log.i("sube", selectedImageByte.toString())
        } ?: run {
            // No se seleccionó ninguna imagen nueva, mantener la imagen actual
            selectedImageByte = binding.userPhoto.drawable?.let { drawable ->
                if (drawable is BitmapDrawable) {
                    bitmapToByteArray(drawable.bitmap)
                } else {
                    null
                }
            }
            Log.i("cargada", "${selectedImageByte}")
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }


    val retrofit = ApiClient.web

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //el firebase
        FirebaseApp.initializeApp(this)
        storage = FirebaseStorage.getInstance()


        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Autenticación exitosa, continuar con el flujo de la aplicación
                } else {
                    // Autenticación fallida, mostrar un mensaje de error
                    Log.e("ViewProfile", "Error al iniciar sesión anónimamente", task.exception)

                }
            }


        binding.userPhoto.setOnClickListener {
            pickMedia.launch("image/*")
        }


        //Obteener el perfil del usuario
        lifecycleScope.launch(Dispatchers.IO) {

            val response: Response<dataProfileUser> = retrofit.getprofileUser(token)


            Log.i("Albañil", response.toString())
            if (response.isSuccessful) {
                val myResponse = response.body()
                if (myResponse != null) {
                    idUserFoto= myResponse.userFound._id
                }


                withContext(Dispatchers.Main) {
                    myResponse?.let {

                        binding.idUser.setText(myResponse.userFound._id)
                        binding.name.setText(myResponse.userFound.Nombres_Mv)
                        binding.email.setText(myResponse.userFound.Email_Mv)
                        binding.identification.setText(myResponse.userFound.NumeroIdent_Mv.toString())
                        binding.brithday.setText(myResponse.userFound.FechaNac_Mv)
                        binding.phoneNumber.setText(myResponse.userFound.NumeroTel_Mv.toString())

                        if (myResponse.userFound.ImageUser.isEmpty()) {
                            Picasso.get()
                                .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png")
                                .into(binding.userPhoto)

                        } else {
                            Picasso.get()
                                .load(myResponse.userFound.ImageUser)
                                .into(binding.userPhoto)

                        }


                    }


                }

            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ViewProfile,
                        "Fallo al obtener los datos por q?? nose",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }


        val btnGuardar = binding.btnGuardar


        btnGuardar.setOnClickListener {
            val token = token
            //si el user no Actualiza photo
            lifecycleScope.launch(Dispatchers.IO) {
                val response: Response<dataProfileUser> = retrofit.getprofileUser(token)
                val myResponse = response.body()

                val imageFilePart = selectedImageByte?.let {
                    MultipartBody.Part.createFormData(
                        "image",
                        "image.jpg",
                        it.toRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                // Verificar si se seleccionó una nueva imagen
                if (imageFilePart != null) {
                    subirImagen(imageFilePart)
                } else {
                    // No se seleccionó una nueva imagen, actualizar otros datos sin cambiar la imagen
                    val currentImageUrl = myResponse?.userFound?.ImageUser ?: ""
                    upUser(currentImageUrl)
                }

            }


        }

        val editTexts = listOf(
            binding.userPhoto,
            binding.name,
            binding.email,
            binding.identification,
            binding.brithday,

            binding.phoneNumber
        )

        editTexts.forEach { it.isEnabled = false }

        binding.btnEdit.setOnClickListener {
            editTexts.forEach { it.isEnabled = true }
            btnGuardar.visibility = View.VISIBLE
            binding.btnCancelar.visibility = View.VISIBLE

            binding.btnEdit.visibility = View.GONE

        }
        binding.btnCancelar.setOnClickListener {
            editTexts.forEach { it.isEnabled = false }
            binding.btnCancelar.visibility = View.GONE
            binding.btnEdit.visibility = View.VISIBLE
        }


    }

    private fun upUser(imgUsuario: String) {

        val idUser = binding.idUser.text.toString()
        val userName = binding.name.text.toString()
        val userEmail = binding.email.text.toString()
        val userIdenti = binding.identification.text.toString()
        val userBrithday = binding.brithday.text.toString()

        val userPhoneNumber = binding.phoneNumber.text.toString()



        val updateData = updateUser(
            userName, userEmail, userIdenti, userBrithday, userPhoneNumber, imgUsuario
        )
        lifecycleScope.launch(Dispatchers.IO) {

            val response = retrofit.updateUser(idUser, updateData, token)
            if (response.isSuccessful) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ViewProfile, "Usuario Actualizado", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@ViewProfile, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            } else {
                Toast.makeText(
                    this@ViewProfile, "Algo Salio mal por q nose", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 50, stream)
        return stream.toByteArray()
    }


    //esta es la fun es para subir un archivo de imagen a Firebase
    private fun subirImagen(imageFile: MultipartBody.Part?) {
        imageFile ?: run {
            Toast.makeText(this@ViewProfile, "La URI de la imagen es nula", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val storageRef = storage.reference
        val imageRef = storageRef.child("imgProfiles/id=${idUserFoto}.jpg")

        imageRef.putFile(imageUri!!)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Log.d("GarageActivity", "Enlace de descarga de la imagen: $downloadUrl")
                    Toast.makeText(this@ViewProfile, "Imagen subida con éxito", Toast.LENGTH_SHORT)
                        .show()
                    upUser(downloadUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@ViewProfile, "Error al subir la imagen", Toast.LENGTH_SHORT)
                    .show()
            }
    }


}