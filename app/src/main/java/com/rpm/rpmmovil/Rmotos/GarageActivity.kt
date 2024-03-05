package com.rpm.rpmmovil.Rmotos

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Rmotos.model.Apis.RegisterMoto
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GarageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var retrofit: Retrofit
//    private lateinit var registerMotoService: RegisterMoto
    private var selectedImageUri: Uri? = null
    private val apiService = retrofit.create(RegisterMoto::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val token = sharedPreferences.getString("token", null)


        // Configurar Retrofit y el servicio
        retrofit = Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        // Registrar la selección de imágenes
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                // Imagen seleccionada
                selectedImageUri = uri
            } else {
                // No se seleccionó ninguna imagen
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el evento click del botón de registro
        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val placa = binding.placamoto.text.toString()
            val nombre = binding.motonombre.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumo.text.toString()

            // Validar campos obligatorios
            if (marca.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Crear un objeto DataItemMotos con la información de la motocicleta
                val motoRegisterData = DataItemMotos(
                    motonom = nombre,
                    motomarca = marca,
                    motomodel = modelo,
                    motovers = version.toIntOrNull() ?: 0,
                    consumokmxg = consumo.toIntOrNull() ?: 0,
                    cilimoto = cilindraje,
                    imagemoto = selectedImageUri?.toString() ?: ""
                )

                // Registrar la motocicleta usando Retrofit
                val call: Call<DataItemMotos> =apiService.PostRegisterMoto(motoRegisterData,"Bread $token")

                // Usar el token obtenido durante el inicio de sesión para la autenticación
//                val headers = HashMap<String, String>()
//                headers["Authorization"] = "Bearer $token"

                call.enqueue(object : Callback<DataItemMotos> {
                    override fun onResponse(
                        call: Call<DataItemMotos>,
                        response: Response<DataItemMotos>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Registro de moto exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error en el registro de moto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DataItemMotos>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Error en el registro de moto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}
