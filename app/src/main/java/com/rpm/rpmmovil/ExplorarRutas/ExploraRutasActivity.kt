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

        listView = binding.listView2

        val retrofit = Retrofit.Builder()
            .baseUrl("https://rpm-back-end.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val connection = retrofit.create(ApiServiceRutas::class.java)
        val call = connection.getAllRutas()

        call.enqueue(object : Callback<DataAllRutas> {
            override fun onResponse(call: Call<DataAllRutas>, response: Response<DataAllRutas>) {
                if (response.isSuccessful) {
                    val rutas: DataAllRutas? = response.body()
                    rutas?.let {
                        val adapter = CustomArrayAdapter(
                            this@ExploraRutasActivity,
                            android.R.layout.simple_list_item_1,
                            it.ruta
                        )
                        listView.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@ExploraRutasActivity, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataAllRutas>, t: Throwable) {
                Toast.makeText(this@ExploraRutasActivity, "Error en la solicitud", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private class CustomArrayAdapter(
        context: AppCompatActivity,
        resource: Int,
        objects: List<DataRutas>
    ) : ArrayAdapter<DataRutas>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)

            val item = getItem(position)
            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val nombre = item?.NombreRuta;
            val kMtotales = item?.KmTotalesRuta;
            val calificacion = item?.CalificacionRuta;
            val fotoRuta = item?.FotoRuta;
            text1.text = "Nombre: ${nombre} - Kilometros: ${kMtotales} - Calificacion: ${calificacion} "
            try {
                Picasso.get().load(fotoRuta).into(imageView)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return view
        }
    }
}
