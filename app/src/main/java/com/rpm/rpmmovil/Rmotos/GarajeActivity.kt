package com.rpm.rpmmovil.Rmotos

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Rmotos.model.DataRMotos
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
                // Crea un objeto DataRMotos con la información de la moto
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val registerMotoService = retrofit.create(RegisterMoto::class.java)

                val moto = DataRMotos(
                    MotoNombre = marca,
                    ModeloMoto = modelo,
                    MarcaMoto = nombre,
                    VersionMoto = version.toInt(),
                    ConsumoMotoLx100km = consumo.toInt(),
                    CilindrajeMoto = cilindraje
                )

                // Agrega el token como encabezado a la solicitud
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"

                // Llama a la función PostRegisterMoto de la interfaz RegisterMoto
                val call = registerMotoService.PostRegisterMoto(moto, headers)

                call.enqueue(object : Callback<DataRMotos> {
                    override fun onResponse(call: Call<DataRMotos>, response: Response<DataRMotos>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@GarajeActivity,
                                "Moto Registrada con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@GarajeActivity,
                                "Error al registrar la moto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DataRMotos>, t: Throwable) {
                        Toast.makeText(
                            this@GarajeActivity,
                            "Error al conectar con la API",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

        binding.garage.setOnClickListener {
            // Agrega aquí el código para ir a la pantalla de la lista de motos si es necesario
        }
    }
}
