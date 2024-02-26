package com.rpm.rpmmovil.Rmotos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.rpm.rpmmovil.Model.Garage
import com.rpm.rpmmovil.Model.ManagerDb
import com.rpm.rpmmovil.Rmotos.model.AllMotosDta
import com.rpm.rpmmovil.Rmotos.model.ApiServiceMotosAll
import com.rpm.rpmmovil.databinding.ActivityShowGarageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShowGarageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShowGarageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowGarageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = String;
//
//        val manager = ManagerDb(this)
//
//        val arrayGarage = manager.getDataRmotos()
//
//        val listGarage = binding.listView
//        val arrayAdapter = ArrayAdapter<Garage>(this,android.R.layout.simple_list_item_1,arrayGarage)
//
//        listGarage.adapter = arrayAdapter
//
//        Toast.makeText(this,"Nueva Moto Registrada",Toast.LENGTH_SHORT).show()

        val text = binding.textView2

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val service = retrofit.create(ApiServiceMotosAll::class.java)

        val call =
            service.getAllMotos() // Consulta el Pokémon con ID 1
        call.enqueue(object : Callback<AllMotosDta> {
            override fun onResponse(call: Call<AllMotosDta>, response: Response<AllMotosDta>) {
                if (response.isSuccessful) {
                    val pokemon: AllMotosDta? = response.body()
                    // Haz lo que quieras con la información del Pokémon
                    Toast.makeText(this@ShowGarageActivity, "El consumido es$pokemon", Toast.LENGTH_SHORT)
                        .show()
                    text.text = pokemon.toString()


                } else {
                    // Manejar errores

                }
            }

            override fun onFailure(call: Call<AllMotosDta>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
}