package com.rpm.rpmmovil.Rmotos

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.databinding.ActivityGarajeBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class GarageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGarajeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedImageByte: ByteArray? = null

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val bitmapImage = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))

            val result = bitmapToByteArray(bitmapImage)
            selectedImageByte = result

            binding.imageView.setImageBitmap(bitmapImage)
        } else {
            // No se seleccionó ninguna imagen
            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGarajeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)




        binding.garage.setOnClickListener {
            val intent = Intent(this, ShowGarageActivity::class.java)
            startActivity(intent)
        }

        val imgmoto = binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Registrar la selección de imágenes

        // Configurar el evento click del botón de registro
        binding.register.setOnClickListener {
            val marca = binding.marcamoto.text.toString()
            val modelo = binding.modelomoto.text.toString()
            val cilindraje = binding.cilindrajemoto.text.toString()
            val placa = binding.placamoto.text.toString()
            val nombre = binding.motonombre.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumo.text.toString()
            val imagenmoto = imgmoto.toString()

            // Validar campos obligatorios
            if (marca.isEmpty() || cilindraje.isEmpty() || placa.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                // Crear un objeto DataItemMotos con la información de la motocicleta
                val motoRegisterData = DataItemMotos(
                    motonom = nombre,
                    motomarca = marca,
                    motomodel = modelo,
                    motovers = version.toIntOrNull() ?: 0,
                    consumokmxg = consumo.toIntOrNull() ?: 0,
                    cilimoto = cilindraje,
                    imagemoto = imagenmoto
                )

                registerNewMot(motoRegisterData)
            }
        }
    }

    private fun bitmapToByteArray(bitmapImage: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun registerNewMot(motoRegisterData: DataItemMotos) {
        lifecycleScope.launch {
            try {
                val token = sharedPreferences.getString("token", null)
                val result = ApiClient.web.PostRegisterMoto(motoRegisterData, token!!)
                Log.e("TAG", "${result}", )
            }catch (e:Exception){
                Log.e("TAG", "${e}", )
                Toast.makeText(this@GarageActivity, "Ocurrio un error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
