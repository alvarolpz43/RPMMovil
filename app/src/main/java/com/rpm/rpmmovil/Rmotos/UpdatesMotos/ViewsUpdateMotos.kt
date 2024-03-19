package com.rpm.rpmmovil.Rmotos.UpdatesMotos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.model.updateMoto
import com.rpm.rpmmovil.databinding.ActivityViewsUpdateMotosBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.utils.AppRPM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewsUpdateMotos : AppCompatActivity() {
    private lateinit var binding: ActivityViewsUpdateMotosBinding
    val token = AppRPM.prefe.getToken().toString()
    val retrofit = ApiClient.web
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewsUpdateMotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserMotosId()
    }

    private fun getUserMotosId() {
        val motonom = intent.getStringExtra("motonom")
        val motomodel = intent.getStringExtra("motomodel")
        val motomarca = intent.getStringExtra("motomarca")
        val motovers = intent.getStringExtra("motovers")
        val consumokmxg = intent.getStringExtra("consumokmxg")
        val cilimoto = intent.getStringExtra("cilimoto")

        //para usarlo en el edit
        val idUserMoto = intent.getStringExtra("idMoto")

        binding.motonombre.setText(motonom)
        binding.motomodelo.setText(motomodel)
        binding.marcamoto.setText(motomarca)
        binding.versionmoto.setText(motovers)
        binding.consumokmxg.setText(consumokmxg)
        binding.cilimoto.setText(cilimoto)

        //inputs
        val editTexts = listOf(
            binding.motomodelo,
            binding.imagemoto,
            binding.motonombre,
            binding.marcamoto,
            binding.cilimoto,
            binding.versionmoto,
            binding.consumokmxg
        )
        editTexts.forEach { it.isEnabled = false }
        binding.btnEdit.setOnClickListener {

            editTexts.forEach { it.isEnabled = true }
        }


        binding.btnGuardar.setOnClickListener {

            val nombre = binding.motonombre.text.toString()
            val modelo = binding.motomodelo.text.toString()
            val marca = binding.marcamoto.text.toString()
            val version = binding.versionmoto.text.toString()
            val consumo = binding.consumokmxg.text.toString()
            val cilindraje = binding.cilimoto.text.toString()
            val objMotoUbdate = updateMoto(
                nombre, marca, modelo, version.toInt(), consumo.toInt(), cilindraje
            )
            lifecycleScope.launch(Dispatchers.IO) {

                val response = retrofit.updateMoto(idUserMoto.toString(), objMotoUbdate, token)
                Log.i("Respuesta", "$response")
                if (response.isSuccessful) {
                    Log.i("Response", "Todo bn")
                    withContext(Dispatchers.Main) {
                        val intent: Intent =
                            Intent(this@ViewsUpdateMotos, ShowGarageActivity::class.java)
                        startActivity(intent)
                    }

                } else {
                    Log.i("Respoonss", "Error en la Respuesta")

                }


            }

        }


    }
}



