package com.rpm.rpmmovil.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.databinding.ActivityViewProfileBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.model.dataProfileUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewProfile : AppCompatActivity() {

    private lateinit var binding: ActivityViewProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val retrofit=getRetrofit()
        Toast.makeText(this, "ViewProfile", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO) {
            val response:Call<dataProfileUser> = retrofit.create(ApiServices::class.java).getprofileUser()
            val r =response.execute().body()

            try {


                Log.i("response",r.toString())
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@ViewProfile, "Fallo al obtener los datos por q?? nose", Toast.LENGTH_SHORT).show()
                }




            }
        }



        val btnGuardar=binding.btnGuardar
        val editTexts = listOf(binding.name, binding.email, binding.identification, binding.brithday, binding.password, binding.phoneNumber)

        editTexts.forEach { it.isEnabled = false }


        binding.btnEdit.setOnClickListener {
            editTexts.forEach { it.isEnabled = true }
            btnGuardar.visibility = View.VISIBLE

        }

    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}