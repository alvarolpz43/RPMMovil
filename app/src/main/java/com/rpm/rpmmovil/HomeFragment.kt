package com.rpm.rpmmovil


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity
import com.rpm.rpmmovil.ExplorarRutas.model.ApiServiceRutas
import com.rpm.rpmmovil.ExplorarRutas.model.DataRutasRespose
import com.rpm.rpmmovil.ExplorarRutas.model.RutaAdapter
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
import com.rpm.rpmmovil.Routes.ListarRutasActivity
import com.rpm.rpmmovil.Routes.MapActivity
import com.rpm.rpmmovil.databinding.FragmentHomeBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.profile.ViewProfile
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var retrofit2: Retrofit
    private lateinit var adapter: RutaAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    val retrofit = ApiClient.web
    val token = AppRPM.prefe.getToken()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofit2 = getRetrofit()

        adapter = RutaAdapter { rutaId -> navigateMapaRuta(rutaId) }

        binding.rvRutas.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.adapter
        }


        buttonsFunction()


            CoroutineScope(Dispatchers.IO).launch {
                datosProfile()
                try {
                    val myResponse: Response<DataRutasRespose> =
                        retrofit2.create(ApiServiceRutas::class.java).getAllRutas()

                    Log.i("iraa", myResponse.toString())

                    if (myResponse.isSuccessful) {
                        val response: DataRutasRespose? = myResponse.body()

                        if (response != null) {
                            withContext(Dispatchers.Main){
                                adapter.updateList(response.ruta)
                            }



                        }
                    } else {
                        Log.e("Rpm", "Error en la respuesta: ${myResponse.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("Rpm", "Error: ${e.message}", e)

                        // Puedes mostrar un mensaje de error al usuario
//                        Toast.makeText(
//                            requireContext(),
//                            "Error: ${e.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()

                }
            }


    }




    private suspend fun datosProfile() {
        val response: Response<dataProfileUser> = retrofit.getprofileUser(token.toString())
        val myResponse = response.body()

        withContext(Dispatchers.Main) {
            binding.saludo.text = "Hola!!, ${myResponse!!.userFound.Nombres_Mv}"
            Picasso.get()
                .load(myResponse.userFound.ImageUser)
                .into(binding.userProfile)

            binding.userProfile.setOnClickListener {
                val intent = Intent(requireContext(), ViewProfile::class.java)
                startActivity(intent)
            }
        }
    }

    private fun navigateMapaRuta(id:String) {
        val intent = Intent(requireContext(),MapActivity::class.java)
        intent.putExtra(MapActivity.EXTRA_ID,id)
        startActivity(intent)
    }

    private fun buttonsFunction() {
        val btnTrazarRuta = binding.btnTrazarRuta
        btnTrazarRuta.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
        val btnListarRuta = binding.btnTusRutas
        btnListarRuta.setOnClickListener {
            val intent = Intent(requireContext(), ListarRutasActivity::class.java)
            startActivity(intent)
        }
        //binding.btnGaraje.setOnClickListener {
          //  val intent = Intent(requireContext(), GarageActivity::class.java)
            //startActivity(intent)
        //}
        binding.btnGaraje.setOnClickListener {
            // Aquí se inicia directamente la ShowGarageActivity
            val intent = Intent(requireContext(), ShowGarageActivity::class.java)
            startActivity(intent)
        }
        binding.btnExplorar.setOnClickListener {
            val intent = Intent(requireContext(), ExploraRutasActivity::class.java)
            startActivity(intent)
        }
    }





    }




    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constains.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



