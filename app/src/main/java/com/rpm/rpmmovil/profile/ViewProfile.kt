package com.rpm.rpmmovil.profile

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.model.dataProfileUser
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

    companion object {
        const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = AppRPM.prefe.getToken().toString()


        val retrofit = getRetrofit()


//        Toast.makeText(this, "${token}", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<dataProfileUser> =
                retrofit.create(ApiServices::class.java).getprofileUser(token)


            Log.i("Albañil", response.toString())
            if (response.isSuccessful) {
                val myResponse = response.body()

                withContext(Dispatchers.Main) {
                    myResponse?.let {
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

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}