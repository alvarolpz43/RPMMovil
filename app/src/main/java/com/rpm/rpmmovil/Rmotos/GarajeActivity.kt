package com.rpm.rpmmovil.Rmotos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Rmotos.model.DataRMotos
import com.rpm.rpmmovil.Rmotos.model.RegisterMoto
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GarajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding

    // Ajusta la URL de tu API
    private val BASE_URL = "https://tudominio.com/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val placa = binding.placamoto.text.toString()
            val nombre= binding.motonombre.text.toString()
            val version= binding.versionmoto.text.toString().toInt()
            val consumo= binding.consumo.text.toString().toInt()


            if (marca.isEmpty() || modelo.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Crea un objeto DataRMotos con la información de la moto
                val moto = DataRMotos(
                    MotoNombre = marca,
                    ModeloMoto = modelo,
                    MarcaMoto = nombre, // Ajusta según tu necesidad
                    VersionMoto =version,
                    ConsumoMotoLx100km = consumo,
                    CilindrajeMoto = cilindraje,
                    FotoMoto = "URL de la foto"
                )

                // Inicia Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Crea la instancia de la interfaz de la API
                val registerMotoService = retrofit.create(RegisterMoto::class.java)

                // Realiza la llamada a la API para registrar la moto
                val call = registerMotoService.PostRegisterMoto(moto)

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
