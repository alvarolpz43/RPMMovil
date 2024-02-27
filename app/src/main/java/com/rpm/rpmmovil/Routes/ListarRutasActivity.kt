package com.rpm.rpmmovil.Routes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.Model.ManagerDb
import com.rpm.rpmmovil.databinding.ActivityListarRutasBinding

class ListarRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListarRutasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListarRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = ManagerDb(this)
        val arrayRoute = manager.getData()

        // Crear una lista de nombres de rutas
        val nombreDeRutas = arrayRoute.map { it.rutaN }

        val listRoute = binding.listaRutas
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombreDeRutas)

        listRoute.adapter = arrayAdapter
        Toast.makeText(this, "Rutas listadas", Toast.LENGTH_SHORT).show()
    }


}