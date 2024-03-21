    package com.rpm.rpmmovil.Rmotos

    import DataMotosResponse
    import android.content.Intent
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.widget.SearchView
    import androidx.core.view.isVisible
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.rpm.rpmmovil.Model.Constains.Companion.BASE_URL
    import com.rpm.rpmmovil.Rmotos.model.Apis.ApiServiceMotos
    import com.rpm.rpmmovil.Rmotos.model.RecyclerV.MotoAdapter
    import com.rpm.rpmmovil.databinding.ActivityShowGarageBinding
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import retrofit2.Response
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory

    class ShowGarageActivity : AppCompatActivity() {
        private lateinit var binding: ActivityShowGarageBinding
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var adapter: MotoAdapter
        private lateinit var retrofit: Retrofit

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityShowGarageBinding.inflate(layoutInflater)
            setContentView(binding.root)

            retrofit = getRetrofit()
            initUI()
            searchAllMotos()
        }

        private fun initUI() {
            binding.btnAgregarMoto.setOnClickListener {
                val intent = Intent(this, GarageActivity::class.java)
                startActivity(intent)
            }

            adapter = MotoAdapter(emptyList()) {
                // Logic when an item in the list is selected
            }

            binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchAllMotos()  // Switch to the function to search all routes
                    return false
                }

                override fun onQueryTextChange(newText: String?) = false
            })

            binding.rvMotos.setHasFixedSize(true)
            binding.rvMotos.layoutManager = LinearLayoutManager(this)
            binding.rvMotos.adapter = adapter
        }



        private fun searchAllMotos() {
            binding.progressBar2.isVisible = true
            CoroutineScope(Dispatchers.IO).launch {
                val token = obtenerToken()
                try {
                    val myResponse: Response<DataMotosResponse> =
                        retrofit.create(ApiServiceMotos::class.java)
                            .getAllMotos(token)

                    if (myResponse.isSuccessful) {
                        val response: DataMotosResponse? = myResponse.body()
                        Log.e("TAG", "${response}",)
                        if (response != null) {
                            runOnUiThread {
                                adapter.updatelist(response.moto)
                                binding.progressBar2.isVisible = false
                            }
                        }
                    } else {
                        Log.e("Rpm", "Error en la respuesta: ${myResponse.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("Rpm", "Error: ${e.message}", e)
                    runOnUiThread {
                        // You can display an error message to the user
                        Toast.makeText(
                            this@ShowGarageActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBar2.isVisible = false
                    }
                }
            }
        }

        private fun updateMotos() {

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

