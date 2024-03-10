package com.rpm.rpmmovil.SelectMoto

import SelectMotoClient
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.SelectMoto.model.DtaSelectMoto
import com.rpm.rpmmovil.databinding.ActivitySelectMotosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectMotosActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectMotosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectMotosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)



        val apiService = SelectMotoClient.apiService
        val motoId = "65ed5338c7c7e85d0ca78eb6"

        apiService.getMotoById(motoId).enqueue(object : Callback<DtaSelectMoto> {
            override fun onResponse(call: Call<DtaSelectMoto>, response: Response<DtaSelectMoto>) {
                if (response.isSuccessful) {
                    val moto = response.body()
                    if (moto != null) {
                        val id = moto.id
                        val modelo = moto.modelo
                        val consumo = moto.consumo
                        val nombre = moto.nombre

                        binding.textViewModelo.text = "Modelo: $modelo"
                        binding.textViewConsumo.text = "Consumo (L/100km): $consumo"
                        binding.textViewNombre.text = "Nombre: $nombre"
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<DtaSelectMoto>, t: Throwable) {

            }
        })
    }
    }
