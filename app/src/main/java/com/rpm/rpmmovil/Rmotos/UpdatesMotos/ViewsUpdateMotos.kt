package com.rpm.rpmmovil.Rmotos.UpdatesMotos

import DataItemMotos
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.updateMoto
import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.ViewProfile
import com.rpm.rpmmovil.utils.AppRPM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
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


    val token = AppRPM.prefe.getToken().toString()


    //
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

        //Desde aqui llamamos a las motos de los usurios

        getUserMotosId()


        val btnGuardar = binding.btnGuardar


        btnGuardar.setOnClickListener {
            val token = token


        }



//        binding.btnGuardar.setOnClickListener {
//            editTexts.forEach { it.isEnabled = true }
//            btnGuardar.visibility = View.VISIBLE
//
//
//        }


    }

    private fun getUserMotosId() {
        val motonom = intent.getStringExtra("motonom")
        val motomodel = intent.getStringExtra("motomodel")
        val motomarca = intent.getStringExtra("motomarca")
        val motovers = intent.getStringExtra("motovers")
        val consumokmxg = intent.getStringExtra("consumokmxg")
        val cilimoto = intent.getStringExtra("cilimoto")

        //para usarlo en el edit
        val idUserMoto= intent.getStringExtra("idMoto")

        binding.motonombre.setText(motonom)
        binding.motomodelo.setText(motomodel)
        binding.marcamoto.setText(motomarca)
        binding.versionmoto.setText(motovers)
        binding.consumokmxg.setText(consumokmxg)
        binding.cilimoto.setText(cilimoto)

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


        val nombre= binding.motonombre.text.toString()
        val modelo= binding.motomodelo.text.toString()
        val marca= binding.marcamoto.text.toString()
        val version= binding.versionmoto.text.toString()
        val consumo= binding.consumokmxg.text.toString()
        val cilindraje=binding.cilimoto.text.toString()



        val objMotoUbdate=updateMoto (
            nombre, marca,modelo,version.toInt(),consumo.toInt(),cilindraje
        )

        binding.btnEdit.setOnClickListener {

            editTexts.forEach { it.isEnabled = true }
        }

        binding.btnGuardar.setOnClickListener{


            Toast.makeText(this, "${nombre}", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch(Dispatchers.IO) {
                val retrofit=getRetrofit()
                val response= retrofit.create(ApiServices::class.java).updateMoto(idUserMoto!!,objMotoUbdate,token)
                Log.i("El Aldair", "$response")

                if (response.isSuccessful){
                    val myResponse=response.body()
                    Toast.makeText(this@ViewsUpdateMotos, "${myResponse}", Toast.LENGTH_SHORT).show()

                }else{
                    Log.i("Respoonss", "Error en la Respuesta")

                }



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

//                        upUser(downloadUrl)
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
