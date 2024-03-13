package com.rpm.rpmmovil.Usermotos


import MotosAdapter
import Usermoto
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.Usermotos.model.ApiServiceMotouser
import com.rpm.rpmmovil.Usermotos.model.MotosResponse
import com.rpm.rpmmovil.Usermotos.model.RetrofitClient
import com.rpm.rpmmovil.databinding.ActivityUserMotosBinding
import com.rpm.rpmmovil.presupuesto.Presupuesto
import com.rpm.rpmmovil.presupuesto.model.PresupuestoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserMotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMotosBinding
    private lateinit var motosAdapter: MotosAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiServiceMotouser
    private lateinit var presupuestoService: PresupuestoService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        apiService = RetrofitClient.apiService
        presupuestoService = RetrofitClient.presupuestoService

        val token = sharedPreferences.getString("token", null)
        Log.d("UserMotosActivity", "Token: $token")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: MotosResponse? = apiService.getMotos(token ?: "")

                withContext(Dispatchers.Main) {
                    response?.let { motosResponse ->
                        val motosList: List<Usermoto> = motosResponse.motos
                        if (motosList.isEmpty()) {
                            Toast.makeText(this@UserMotosActivity, "El usuario no tiene motos registradas", Toast.LENGTH_SHORT).show()
                        } else {

                            val primerMoto = motosList[0]
                            Log.d("UserMotosActivity", "ID: ${primerMoto._id}")
                            Log.d("UserMotosActivity", "Nombre: ${primerMoto.MotoNombre}")
                            Log.d("UserMotosActivity", "Modelo: ${primerMoto.ModeloMoto}")
                            Log.d("UserMotosActivity", "Marca: ${primerMoto.MarcaMoto}")
                            Log.d("UserMotosActivity", "Consumo: ${primerMoto.ConsumoMotoLx100km}")


                            enviarConsumoAlPresupuesto(primerMoto.ConsumoMotoLx100km)

                            motosAdapter = MotosAdapter(motosList)
                            binding.recyclerView.adapter = motosAdapter
                        }
                    } ?: run {
                        Toast.makeText(this@UserMotosActivity, "No se encontraron datos de motos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@UserMotosActivity, "Error al obtener datos de motos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para enviar el consumo al endpoint del presupuesto
    private fun enviarConsumoAlPresupuesto(consumoMotoLx100km: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Crear la instancia de la solicitud
                val presupuestoRequest = Presupuesto(consumoMotoLx100km)
                Log.d("UserMotosActivity", "Datos a enviar al backend: $presupuestoRequest")
                // Enviar la solicitud utilizando Retrofit
                val response = presupuestoService.enviarPresupuesto(presupuestoRequest)

                // Manejar la respuesta si es necesario
                withContext(Dispatchers.Main) {
                    // Aquí puedes manejar la respuesta si es necesario
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserMotosActivity, "Error al enviar el consumo al presupuesto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}