package com.rpm.rpmmovil.Routes

import android.R
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Model.Route
import com.rpm.rpmmovil.Routes.apiRoute.AllRutes
import com.rpm.rpmmovil.Routes.apiRoute.RoutesApiService
import com.rpm.rpmmovil.databinding.ActivityListarRutasBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListarRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarRutasBinding
    val urlBase = "https://rpm-back-end.vercel.app/api/"

    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        Toast.makeText(this, "${token}", Toast.LENGTH_SHORT).show()

        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RoutesApiService::class.java)

        val call =
        service.getRutas()

        call.enqueue(object :Callback<AllRutes>{
            override fun onResponse(call: Call<AllRutes>, response: Response<AllRutes>) {
                if (response.isSuccessful) {
                    val pokemones: AllRutes? = response.body()
                    Toast.makeText(
                        this@ListarRutasActivity,
                        "El consumo es ${pokemones}",
                        Toast.LENGTH_SHORT
                    ).show()
//                    text.text = pokemones.toString()




                    val listRoute = binding.list
                    val arrayAdapter = ArrayAdapter<AllRutes>(
                        this@ListarRutasActivity,
                        R.layout.simple_list_item_1,
                    )

                    listRoute.adapter = arrayAdapter


                } else {

                }
            }

            override fun onFailure(call: Call<AllRutes>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }


}
