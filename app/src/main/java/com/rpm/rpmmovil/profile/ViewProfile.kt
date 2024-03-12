package com.rpm.rpmmovil.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.profile.model.updateUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class ViewProfile : AppCompatActivity() {

    private lateinit var binding: ActivityViewProfileBinding

    //pick
    private var selectedImageByte: ByteArray? = null

    //pick media
    val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

            val result = bitmapToByteArray(bitmapImage)
            selectedImageByte = result

            binding.userPhoto.setImageBitmap(bitmapImage)
        } else {
            // No se seleccionó ninguna imagen
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    //


    companion object {
        const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }


    private var idUser = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = AppRPM.prefe.getToken().toString()
        val retrofit = getRetrofit()

        //aqui se gaurda solo ela imagen

        val userPhoto = binding.userPhoto.setOnClickListener {
            //lo que puede seleccionar aqui de tipo solo imagen
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }


        //Obteener el perfil del usuario
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
                        Picasso.get().load(myResponse.userFound.ImageUser).into(binding.userPhoto)


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


            //
            val idUser = binding.idUser.text.toString()
            val userName = binding.name.text.toString()
            val userEmail = binding.email.text.toString()
            val userIdenti = binding.identification.text.toString()
            val userBrithday = binding.brithday.text.toString()
//            val userPassword = binding.password.text.toString()
            val userPhoneNumber = binding.phoneNumber.text.toString()
            val uPhoto = selectedImageByte

            val updateData = updateUser(
                userName, userEmail, userIdenti, userBrithday, userPhoneNumber, uPhoto.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {

                val response =
                    retrofit.create(ApiServices::class.java).updateUser(idUser, updateData, token)
                if (response.isSuccessful) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ViewProfile, "Usuario Actualizado", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@ViewProfile, MainActivity::class.java)
                        startActivity(intent)


                    }

                } else {
                    Toast.makeText(
                        this@ViewProfile, "Algo Salio mal por q nose", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        val editTexts = listOf(
            binding.userPhoto,
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


    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}