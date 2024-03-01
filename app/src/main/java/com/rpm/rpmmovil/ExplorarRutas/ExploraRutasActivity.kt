package com.rpm.rpmmovil.ExplorarRutas

import android.os.Bundle
import android.view.inputmethod.InputMethodManager

import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.ExplorarRutas.model.ApiServiceRutas
import com.rpm.rpmmovil.ExplorarRutas.model.rutaAdapter
import com.rpm.rpmmovil.databinding.ActivityExploraRutasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExploraRutasActivity : AppCompatActivity(), OnQueryTextListener {
    private lateinit var binding: ActivityExploraRutasBinding
    private lateinit var adapter:rutaAdapter
    private val rutaImages = mutableListOf<String>()
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploraRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.setOnQueryTextListener(this)
        initRecyclerView()



        }

    private fun initRecyclerView() {
        adapter=rutaAdapter(rutaImages)
        binding.rvRutas.layoutManager = LinearLayoutManager(this)
        binding.rvRutas.adapter = adapter
    }

    private fun getRetrofit():Retrofit{
        return  Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private  fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiServiceRutas::class.java).getAllRutas("$query/ruta")
            val rutas = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    val images = rutas?.images ?: emptyList()
                    rutaImages.clear()
                    rutaImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }else{
                    showError()
                }
                hideKeyBoard()
            }

        }
    }

    private fun hideKeyBoard() {
      val imm=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken,0)
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByName(query.toLowerCase())
        }
        return  true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return  true
    }
}

