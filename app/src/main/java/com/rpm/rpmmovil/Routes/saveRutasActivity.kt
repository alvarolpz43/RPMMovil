package com.rpm.rpmmovil.Routes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.rpm.rpmmovil.MainActivity
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Model.ManagerDb
import com.rpm.rpmmovil.databinding.ActivitySaveRutasBinding

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

        val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
        val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()

        binding.cordenadasRutaInicio.text = cordenadasInicio
        binding.cordenadasRutaFinal.text = cordenadasFinal

        binding.btnGuardarRoute.setOnClickListener {
            guardarRuta()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Método para subir una imágen desde la galería.
        Constains.URI=binding.imagenRuta

        binding.imageButton.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    fun guardarRuta() {
        val nombreRuta = binding.nombreRuta.text.toString()
        val detalleRuta = binding.detallesRuta.text.toString()
        val cordenadasInicio = intent.extras?.getString("cordenadasInicio").orEmpty()
        val cordenadasFinal = intent.extras?.getString("cordenadasFinal").orEmpty()
        val imagenRuta = Constains.URI.toString()


        val manager = ManagerDb(this)
        manager.insertRoute(nombreRuta, cordenadasInicio, cordenadasFinal, detalleRuta, imagenRuta)

        Toast.makeText(this, "Ruta guardada: $nombreRuta", Toast.LENGTH_SHORT).show()


    }


}
