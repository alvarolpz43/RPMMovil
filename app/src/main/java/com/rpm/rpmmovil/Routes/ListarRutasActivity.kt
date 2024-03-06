package com.rpm.rpmmovil.Routes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.Routes.RecyclerView.RoutesAdapter
import com.rpm.rpmmovil.Routes.apiRoute.AllRutes
import com.rpm.rpmmovil.Routes.apiRoute.RoutesApiService
import com.rpm.rpmmovil.databinding.ActivityListarRutasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListarRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarRutasBinding
    private lateinit var adapter: RoutesAdapter
    private lateinit var retrofit: Retrofit

    companion object {
        private const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        initUI()
        searchAllRutas()  // Llama a la función para obtener todas las rutas al inicio
    }

    private fun initUI() {
        adapter = RoutesAdapter(emptyList()) {
            // Lógica cuando un elemento de la lista se selecciona
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAllRutas()  // Cambia a la función de búsqueda de todas las rutas
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        binding.rvRutas.setHasFixedSize(true)
        binding.rvRutas.layoutManager = LinearLayoutManager(this)
        binding.rvRutas.adapter = adapter
    }

    private fun searchAllRutas() {
        binding.progressBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val token = obtenerToken()
            try {
                val myResponse: Response<AllRutes> =
                    retrofit.create(RoutesApiService::class.java).getAllUserroutes(token)

                if (myResponse.isSuccessful) {
                    val response: AllRutes? = myResponse.body()

                    if (response != null) {
                        runOnUiThread {
                            adapter.updatelist(response.ruta)
                            binding.progressBar.isVisible = false
                        }
                    }
                } else {
                    Log.e("Rpm", "Error en la respuesta: ${myResponse.code()}")
                }
            } catch (e: Exception) {
                Log.e("Rpm", "Error: ${e.message}", e)
                runOnUiThread {
                    // Puedes mostrar un mensaje de error al usuario
                    Toast.makeText(this@ListarRutasActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun obtenerToken(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }
}




