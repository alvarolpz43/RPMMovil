package com.rpm.rpmmovil.Routes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Model.ManagerDb
import com.rpm.rpmmovil.Routes.apiRoute.PostRoutes
import com.rpm.rpmmovil.databinding.ActivitySaveRutasBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import kotlinx.coroutines.launch

class saveRutasActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveRutasBinding

    //Funcion para tomar la dirección de la imagen que se sube de la galería e insertarla
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            // Imagen seleccionada
            Constains.URI.setImageURI(uri)
        } else {

            // no imagen
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveRutasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //GUARDA LAS CORDENADAS DE LA RUTA
        val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
        val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()
        binding.cordenadasRutaInicio.text = cordenadasInicio
        binding.cordenadasRutaFinal.text = cordenadasFinal

        //BOTON GUARDAR RUTA
        binding.btnGuardarRoute.setOnClickListener {
            //Función de guardar la ruta
            guardarRuta()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //MÉTODO PARA SUBIR UNA FOTO DESDE LA GALERÍA DEL CELULAR
        Constains.URI = binding.imagenRuta
        binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    fun guardarRuta() {

        binding.btnGuardarRoute.setOnClickListener {

            val nombreRuta = binding.nombreRuta.text.toString()
            val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
            val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()
            val kmRuta = Constains.DISTANCIA_RUTA
            val ppto = 5000
            val imagenRuta = Constains.URI.toString()
            val detalleRuta = binding.detallesRuta.text.toString()
            val calificacion = 5
            val motoviajero = "fasfdf5456464"

            val route = PostRoutes(
                nombreRuta,
                cordenadasInicio,
                cordenadasFinal,
                kmRuta,
                ppto,
                imagenRuta,
                detalleRuta,
                calificacion,
                motoviajero
            )

            saveNewRoute(route)

        }
    }

    private fun obtenerToken(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }

    private fun saveNewRoute(postRoute: PostRoutes) {
        lifecycleScope.launch {

            val token = obtenerToken()
            try {
                val result = ApiClient.web.PostSaveRoutes(postRoute, token!!)
                Log.e("TAG", "${result}", )
            }catch (e:Exception){
                Log.e("TAG", "${e}", )
                Toast.makeText(this@saveRutasActivity, "Ocurrio un error", Toast.LENGTH_SHORT).show()
            }
        }
    }

// GUARDAR IMAGEN EN FIREBASE





}
