package com.rpm.rpmmovil.ExplorarRutas

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.ExplorarRutas.model.ApiServiceRutas
import com.rpm.rpmmovil.ExplorarRutas.model.DataAllRutas
import com.rpm.rpmmovil.ExplorarRutas.model.DataRutas
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.databinding.ActivityExploraRutasBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExploraRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExploraRutasBinding
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploraRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()



        }

    private fun initRecyclerView() {
        TODO("Not yet implemented")
    }

    private fun getRetrofit():Retrofit{
        return  Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/rutas/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private  fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiServiceRutas::class.java).getAllRutas("$query/ruta")
            val rutas = call.body()
            if (call.isSuccessful){

            }
        }
    }
    }

