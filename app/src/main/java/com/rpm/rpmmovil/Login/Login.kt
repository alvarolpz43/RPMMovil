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
    private var userToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkSession()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            managerIU()
        }
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
            val token = userToken


            val user = DtaUser(email, password, token)

            val call = apiService.getLogin(user)
            call.enqueue(object : Callback<DtaUser> {
                override fun onResponse(call: Call<DtaUser>, response: Response<DtaUser>) {
                    if (response.isSuccessful) {
                        userToken = response.body()?.Token

                        if (isValidToken(userToken)) {
                            // Guardar el token en SharedPreferences
                            saveTokenToSharedPreferences(userToken)

                            Toast.makeText(
                                applicationContext,
                                "Token: $userToken",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Token no válido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DtaUser>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.registrarse.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    private fun isValidToken(token: String?): Boolean {
        return token != null && token.isNotEmpty()
    }

    private fun saveTokenToSharedPreferences(token: String?) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun checkSession(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return isValidToken(token)
    }
}
