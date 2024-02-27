package com.rpm.rpmmovil.Login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Login.model.ApiServiceRegister
import com.rpm.rpmmovil.Login.model.DtaRegister
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

 val retrofit = Retrofit.Builder()
        .baseUrl("https://rpm-back-end.vercel.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   val apiService = retrofit.create(ApiServiceRegister::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        managerIU()
    }

    private fun managerIU() {
        listenerevents()
    }

    private fun listenerevents() {


        binding.BtnRegister.setOnClickListener {
            val nombre = binding.ETName.text.toString()
            val email = binding.ETEmail.text.toString()
            val password = binding.ETPassword.text.toString()
            val identificationNumber = binding.ETIdentificacion.text.toString().toInt()
            val phoneNumber = binding.ETTelefono.text.toString().toLong()
            val date=binding.ETDate.text.toString()

            val userRegistrationData = DtaRegister(
                Nombres_Mv = nombre,
                Email_Mv = email,
                NumeroIdent_Mv = identificationNumber,
                FechaNac_Mv = date,
                Contraseña_Mv = password,
                NumeroTel_Mv = phoneNumber
            )


            val call: Call<DtaRegister> = apiService.getRegister(userRegistrationData)

            call.enqueue(object : Callback<DtaRegister> {
                override fun onResponse(call: Call<DtaRegister>, response: Response<DtaRegister>) {
                    if (response.isSuccessful) {

                        val intent = Intent(this@Register, MainActivity::class.java)
                        Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    } else {

                        Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DtaRegister>, t: Throwable) {

                    Toast.makeText(applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
