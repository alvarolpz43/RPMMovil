package com.rpm.rpmmovil.Rmotos.UpdatesMotos

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.dataUpdatemoto
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding

import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.ViewProfile
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class ViewsUpdateMotos : AppCompatActivity() {


    private lateinit var binding: ActivityViewsUpdateMotosBinding
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

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
            selectedImageByte = if (currentImageDrawable != null && currentImageDrawable is BitmapDrawable) {
                val currentBitmap = currentImageDrawable.bitmap
                bitmapToByteArray(currentBitmap)
            } else {
                null // Opcionalmente, puedes asignar otro valor en lugar de null
            }

            Log.i("cargada", "${selectedImageByte}")
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }


    private var idMoto: String = ""
    val token = AppRPM.prefe.getToken().toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewsUpdateMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = getRetrofit()


        //el firebase
        FirebaseApp.initializeApp(this)
        storage = FirebaseStorage.getInstance()

        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val moto = task.result?.user

                } else {

                    Log.e("GarageActivity", "Error al iniciar sesión anónimamente", task.exception)
                }
            }
        //aqui se gaurda solo ela imagen


        //lamza la galeria o arch...
        binding.imagemoto.setOnClickListener {
            pickMedia.launch("image/*")
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<dataUpdatemoto> =
                retrofit.create(ApiServices::class.java).getupdateMoto(token)


            Log.i("Albañil", response.toString())
            if (response.isSuccessful) {
                val myResponse = response.body()

                withContext(Dispatchers.Main) {
                    myResponse?.let { response ->
                        response.motoFound?.let { moto ->
                            binding.idMoto.setText(moto._id)
                            binding.motonombre.setText(moto.motonombre)
                            binding.marcamoto.setText(moto.motomarca)
                            binding.cilimoto.setText(moto.cilimoto)
                            binding.versionmoto.setText(moto.motoversion.toString())
                            binding.consumokmxg.setText(moto.consumokmxg.toString())

                            if (moto.imagemoto.isEmpty()) {
                                Picasso.get()
                                    .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png")
                                    .into(binding.imagemoto)
                            } else {
                                Picasso.get()
                                    .load(moto.imagemoto)
                                    .into(binding.imagemoto)
                            }
                        }
                    }



                }

            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ViewsUpdateMotos,
                        "Fallo al obtener los datos por q?? nose",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }


        //por ahora




        val btnGuardar = binding.btnGuardar


        btnGuardar.setOnClickListener {
            val token =token
            //si el user no Actualiza photo
            lifecycleScope.launch(Dispatchers.IO){
                val response: Response<dataProfileUser> =
                    retrofit.create(ApiServices::class.java).getprofileUser(token)
                val myRespons=response.body()
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
                    val currentImageUrl = myRespons?.userFound?.ImageUser ?: ""
                    upUser(currentImageUrl)
                }

            }






        }

        val editTexts = listOf(
            binding.imagemoto,
            binding.motonombre,
            binding.marcamoto,
            binding.cilimoto,
            binding.versionmoto,
            binding.consumokmxg
        )

        editTexts.forEach { it.isEnabled = false }

        binding.btnGuardar.setOnClickListener {
            editTexts.forEach { it.isEnabled = true }
            btnGuardar.visibility = View.VISIBLE


        }




    }

    private fun upUser(imgMoto: String) {
        val retrofit = getRetrofit()
        val idMoto = binding.idMoto.text.toString()
        val nombre = binding.motonombre.text.toString()
        val marca = binding.marcamoto.text.toString()
        val version = binding.versionmoto.text.toString()
        val consumo = binding.consumokmxg.text.toString()

        val cilindraje = binding.cilimoto.text.toString()



        val updateData = updateUser(
            nombre, marca, version, consumo, cilindraje, imgMoto
        )

        lifecycleScope.launch(Dispatchers.IO) {

            val response =
                retrofit.create(ApiServices::class.java).updateUser(idMoto, updateData, token)
            if (response.isSuccessful) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ViewsUpdateMotos, "Usuario Actualizado", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@ViewsUpdateMotos, MainActivity::class.java)
                    startActivity(intent)


                }

            } else {
                Toast.makeText(
                    this@ViewsUpdateMotos, "Algo Salio mal por q nose", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(ViewProfile.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }


    //esta es la fun es para subir un archivo de imagen a Firebase
    private fun subirImagen(imageFile: MultipartBody.Part?) {
        if (imageFile != null ) {
            val storageRef = storage.reference
            val imageRef = storageRef.child("imagenes/imagen_${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { uploadTask ->
                    uploadTask.storage.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Log.d("GarageActivity", "Enlace de descarga de la imagen: $downloadUrl")
                        Toast.makeText(this@ViewsUpdateMotos, "Imagen subida con éxito", Toast.LENGTH_SHORT).show()

                        upUser(downloadUrl)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@ViewsUpdateMotos, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@ViewsUpdateMotos, "La URI de la imagen es nula", Toast.LENGTH_SHORT).show()


        }
    }


}
