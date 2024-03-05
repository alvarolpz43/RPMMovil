package com.rpm.rpmmovil.Rmotos

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Rmotos.model.Apis.RegisterMoto
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GarajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding
    private val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    private lateinit var sharedPreferences: SharedPreferences
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val registerMotoService = retrofit.create(RegisterMoto::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val token = sharedPreferences.getString("token", null)
        Toast.makeText(this, "${token}", Toast.LENGTH_SHORT).show()

        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val placa = binding.placamoto.text.toString()
            val nombre = binding.motonombre.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumo.text.toString()

            if (marca.isEmpty() || modelo.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Create an object DataItemMotos with the motorcycle information
                val moto = DataItemMotos(
                    motonom = nombre,
                    motomodel = modelo,
                    motomarca = marca,
                    motovers = version.toIntOrNull() ?: 0, // Cambiado a Int
                    consumokmxg = consumo.toIntOrNull() ?: 0, // Cambiado a Int
                    cilimoto = cilindraje,
                    imagemoto = ""
                )

                // Use the token obtained during login for authentication
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"

                // Register the motorcycle using Retrofit
                val call: Call<DataMotosResponse> = registerMotoService.PostRegisterMoto(moto, "Bearer $token")

                call.enqueue(object : Callback<DataMotosResponse> {
                    override fun onResponse(call: Call<DataMotosResponse>, response: Response<DataMotosResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "Registro de moto exitoso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "Error en el registro de moto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<DataMotosResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error en el registro de moto", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
