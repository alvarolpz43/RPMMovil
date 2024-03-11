package com.rpm.rpmmovil.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.MenuFragment
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.math.log

class ViewProfile : AppCompatActivity() {

    private lateinit var binding: ActivityViewProfileBinding


    //pick media
    val pickMedia= registerForActivityResult(PickVisualMedia()){uri->
        if (uri!=null){
            binding.userPhoto.setImageURI(uri)
            Log.i("this is th image", uri.toString())

        }else{
            Log.i("Luis", "No seleccionado")

        }

    }
    //


    companion object {
        const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }

    val uri:Uri?=null
    val firebaseStorage: FirebaseStorage?=null
    val firebaseDatabase:FireBaseDatabase ?=null




    private var idUser = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = AppRPM.prefe.getToken().toString()
        val retrofit = getRetrofit()

        binding.userPhoto.setOnClickListener {
            //lo que puede seleccionar aqui de tipo solo imagen
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))


        }




        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<dataProfileUser> =
                retrofit.create(ApiServices::class.java).getprofileUser(token)


            Log.i("Albañil", response.toString())
            if (response.isSuccessful) {
                val myResponse = response.body()

                withContext(Dispatchers.Main) {
                    myResponse?.let {

                        binding.idUser.setText(myResponse.userFound._id)
                        binding.name.setText(myResponse.userFound.Nombres_Mv)
                        binding.email.setText(myResponse.userFound.Email_Mv)
                        binding.identification.setText(myResponse.userFound.NumeroIdent_Mv.toString())
                        binding.brithday.setText(myResponse.userFound.FechaNac_Mv)
                        binding.password.setText(myResponse.userFound.Contraseña_Mv)
                        binding.phoneNumber.setText(myResponse.userFound.NumeroTel_Mv.toString())
                        Picasso.get()
                            .load(myResponse.userFound.ImageUser)
                            .into(binding.userPhoto)


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


            subirImagen()

            //
            val idUser = binding.idUser.text.toString()
            val userName = binding.name.text.toString()
            val userEmail = binding.email.text.toString()
            val userIdenti = binding.identification.text.toString()
            val userBrithday = binding.brithday.text.toString()
            val userPassword = binding.password.text.toString()
            val userPhoneNumber = binding.phoneNumber.text.toString()

            val updateData = updateUser(
                userName,
                userEmail,
                userIdenti,
                userBrithday,
                userPassword,
                userPhoneNumber
            )

            lifecycleScope.launch(Dispatchers.IO) {

                val response = retrofit.create(ApiServices::class.java)
                    .updateUser(idUser, updateData, token)
                if (response.isSuccessful) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ViewProfile, "Usuario Actualizado", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@ViewProfile, MainActivity::class.java)
                        startActivity(intent)


                    }

                } else {
                    Toast.makeText(
                        this@ViewProfile,
                        "Algo Salio mal por q nose",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        val editTexts = listOf(
            binding.name,
            binding.email,
            binding.identification,
            binding.brithday,
            binding.password,
            binding.phoneNumber
        )

        editTexts.forEach { it.isEnabled = false }

        binding.btnEdit.setOnClickListener {
            editTexts.forEach { it.isEnabled = true }
            btnGuardar.visibility = View.VISIBLE
        }


    }

    private fun subirImagen() {
        val reference= firebaseStorage!!.reference.child("Images").child(System.currentTimeMillis().toString()+"")
        reference.putFile(uri!!).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri->
                val model=ImageModel()
                model.image= uri.toString()
                firebaseStorage!!.reference.child("Imagenes")
            }
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}