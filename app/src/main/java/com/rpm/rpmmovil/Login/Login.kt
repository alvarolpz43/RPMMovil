package com.rpm.rpmmovil.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Login.model.ApiServiceLogin
import com.rpm.rpmmovil.Login.model.DtaUser
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.Model.ManagerDb
import com.rpm.rpmmovil.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val manager = ManagerDb(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managerIU()
    }

    private fun managerIU() {
        listenerEvents()
    }

    private fun listenerEvents() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiServiceLogin::class.java)

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            // Crear el objeto DtaUser para enviar al servidor
            val user = DtaUser(email, password)

            // Realizar la solicitud de inicio de sesión al servidor
            val call = apiService.getLogin(user)
            call.enqueue(object : Callback<DtaUser> {
                override fun onResponse(call: Call<DtaUser>, response: Response<DtaUser>) {
                    if (response.isSuccessful) {
                        // Manejar la respuesta exitosa aquí (opcional)
                        Toast.makeText(applicationContext, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Manejar la respuesta de error aquí
                        Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DtaUser>, t: Throwable) {
                    // Manejar la falla de la solicitud aquí
                    Toast.makeText(applicationContext, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.registrarse.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
