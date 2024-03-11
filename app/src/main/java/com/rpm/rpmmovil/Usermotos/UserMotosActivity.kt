package com.rpm.rpmmovil.Usermotos

import MotosAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Usermotos.model.ApiServiceMotouser
import com.rpm.rpmmovil.Usermotos.model.Usermoto
import com.rpm.rpmmovil.databinding.ActivityUserMotosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserMotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMotosBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewMotos
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MotosAdapter()
        recyclerView.adapter = adapter


        val token = getSavedTokenFromSharedPreferences()
        Log.d("UserMotosActivity", "Token obtenido: $token")


        if (isValidToken(token)) {

            fetchUserMotos(token)
        } else {

            showError("El token de autenticación no es válido o está vacío.")
        }
    }

    private fun fetchUserMotos(token: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val apiService = retrofit.create(ApiServiceMotouser::class.java)

        apiService.getUserMotos("Bearer $token").enqueue(object : Callback<List<Usermoto>> {
            override fun onResponse(call: Call<List<Usermoto>>, response: Response<List<Usermoto>>) {
                if (response.isSuccessful) {
                    val userMotos = response.body()
                    userMotos?.let {
                        adapter.submitList(userMotos)
                    } ?: showError("No se encontraron motos para el usuario.")
                } else if (response.code() == 403) {
                    showError("No tienes permisos suficientes para acceder a esta información.")
                } else {
                    val errorMessage = "Error al obtener las motos del usuario: ${response.code()}"
                    showError(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<Usermoto>>, t: Throwable) {
                showError("Error de red: ${t.message}")
            }
        })
    }


    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun isValidToken(token: String): Boolean {
        return token.isNotEmpty()
    }

    private fun getSavedTokenFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }
}
