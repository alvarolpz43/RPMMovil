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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserMotosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMotosBinding
    private lateinit var motosAdapter: MotosAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiServiceMotouser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        apiService = RetrofitClient.apiService

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

                            motosAdapter = MotosAdapter(motosList)
                            binding.recyclerView.adapter = motosAdapter


                            val primerMoto = motosList[0]
                            Log.d("UserMotosActivity", "ID: ${primerMoto._id}")
                            Log.d("UserMotosActivity", "Nombre: ${primerMoto.MotoNombre}")
                            Log.d("UserMotosActivity", "Modelo: ${primerMoto.ModeloMoto}")
                            Log.d("UserMotosActivity", "Marca: ${primerMoto.MarcaMoto}")
                            Log.d("UserMotosActivity", "Consumo: ${primerMoto.ConsumoMotoLx100km}")
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
}