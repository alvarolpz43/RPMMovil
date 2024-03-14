package com.rpm.rpmmovil


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity
import com.rpm.rpmmovil.Model.Constains
import com.rpm.rpmmovil.Rmotos.GarageActivity
import com.rpm.rpmmovil.Routes.ListarRutasActivity
import com.rpm.rpmmovil.Routes.MapActivity
import com.rpm.rpmmovil.databinding.FragmentHomeBinding
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch() {
            datosProfile()


        }


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
        binding.btnGaraje.setOnClickListener {
            val intent = Intent(requireContext(), GarageActivity::class.java)
            startActivity(intent)
        }
        binding.btnExplorar.setOnClickListener {
            val intent = Intent(requireContext(), ExploraRutasActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun datosProfile() {
        //tenemos los datos
        val token = AppRPM.prefe.getToken()
        val retrofit = getRetrofit()

        val response: Response<dataProfileUser> =
            retrofit.create(ApiServices::class.java).getprofileUser(token.toString())
        val myResponse = response.body()

        binding.saludo.setText("Hola!!, ${myResponse!!.userFound.Nombres_Mv} ")

        if (myResponse.userFound.ImageUser.isEmpty()) {
            Picasso.get()
                .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png")
                .into(binding.userProfile)

        } else {
            Picasso.get()
                .load(myResponse.userFound.ImageUser)
                .into(binding.userProfile)

        }


    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constains.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
