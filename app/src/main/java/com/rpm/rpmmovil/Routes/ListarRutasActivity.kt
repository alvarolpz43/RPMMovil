package com.rpm.rpmmovil.Routes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Routes.RecyclerView.RoutesAdapter
import com.rpm.rpmmovil.Routes.apiRoute.UserRoutes
import com.rpm.rpmmovil.databinding.ActivityListarRutasBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.utils.AppRPM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListarRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarRutasBinding
    val token = AppRPM.prefe.getToken()

    private lateinit var adapter: RoutesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //Cambio de Hilo
        lifecycleScope.launch(Dispatchers.IO) {
            getUserRutas()
        }
        initUI()
        searchAllRutas()


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
            val retrofit = getRetrofit()
            val token = AppRPM.prefe.getToken()
            try {
                val myResponse: Response<UserRoutes> =
                    retrofit.create(ApiServices::class.java).getUserRutas(token.toString())

                if (myResponse.isSuccessful) {
                    val response: UserRoutes? = myResponse.body()

                    if (response != null) {
                        runOnUiThread {
                            adapter.updatelist(response.rutas)
                            binding.progressBar.isVisible = false
                        }
                    }else{
                        Toast.makeText(this@ListarRutasActivity, "No hay Rutas, Puedes Crear una!!!!", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Log.e("Rpm", "Error en la respuesta: ${myResponse.code()}")
                }
            } catch (e: Exception) {
                Log.e("Rpm", "Error en: ${e.message}", e)
                runOnUiThread {
                    // Puedes mostrar un mensaje de error al usuario
                    Toast.makeText(this@ListarRutasActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }
        }
    }







    private suspend fun getUserRutas() {
        val retrofit = getRetrofit()
        val myResponse: Response<UserRoutes> =
            retrofit.create(ApiServices::class.java).getUserRutas(token.toString())



        if (myResponse.isSuccessful) {
            val response: UserRoutes? = myResponse.body()

            if (response != null) {
                runOnUiThread {
                    adapter.updatelist(response.rutas)
                    binding.progressBar.isVisible = false
                }

            }
        } else {
            Log.e("Rpm", "Error en la respuesta: ${myResponse.code()}")
        }
    }
}


fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constains.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}


