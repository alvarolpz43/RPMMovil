package com.rpm.rpmmovil.ExplorarRutas


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.ExplorarRutas.model.ApiServiceRutas
import com.rpm.rpmmovil.ExplorarRutas.model.DataRutasRespose
import com.rpm.rpmmovil.ExplorarRutas.model.RutaAdapter
import com.rpm.rpmmovil.Routes.MapActivity
import com.rpm.rpmmovil.Routes.MapActivity.Companion.EXTRA_ID
import com.rpm.rpmmovil.databinding.ActivityExploraRutasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExploraRutasActivity: AppCompatActivity() {
    private lateinit var binding: ActivityExploraRutasBinding
    private lateinit var adapter: RutaAdapter
    private lateinit var retrofit: Retrofit

    companion object {
        private const val BASE_URL = "https://rpm-back-end.vercel.app/api/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploraRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()
        initUI()
        searchAllRutas()  // Llama a la función para obtener todas las rutas al inicio

    }

    private fun initUI() {
        adapter = RutaAdapter{rutaId -> navigateMapaRuta(rutaId)}
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

    private fun navigateMapaRuta(id:String) {
        val intent = Intent(this,MapActivity::class.java)
        intent.putExtra(EXTRA_ID,id)
        startActivity(intent)
    }

    private fun searchAllRutas() {
        binding.progressBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val myResponse: Response<DataRutasRespose> =
                    retrofit.create(ApiServiceRutas::class.java).getAllRutas()

                Log.i("iraa", myResponse.toString())

                if (myResponse.isSuccessful) {
                    val response: DataRutasRespose? = myResponse.body()

                    if (response != null) {
                        runOnUiThread {
                            adapter.updateList(response.ruta)
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
                    Toast.makeText(
                        this@ExploraRutasActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
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
}


