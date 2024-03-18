package com.rpm.rpmmovil


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity
import com.rpm.rpmmovil.Rmotos.GarageActivity
import com.rpm.rpmmovil.Routes.ListarRutasActivity
import com.rpm.rpmmovil.Routes.MapActivity
import com.rpm.rpmmovil.Usermotos.UserMotosActivity
import com.rpm.rpmmovil.databinding.FragmentHomeBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.utils.AppRPM
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Response


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

    val retrofit = ApiClient.web
    val token = AppRPM.prefe.getToken()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch() {
            datosProfile()
        }

        buttonsFunction()

    }


    private suspend fun datosProfile() {
        val response: Response<dataProfileUser> = retrofit.getprofileUser(token.toString())
        val myResponse = response.body()
        binding.saludo.setText("Hola!!, ${myResponse!!.userFound.Nombres_Mv} ")
        Picasso.get()
            .load(myResponse.userFound.ImageUser)
            .into(binding.userProfile)

    }

    private fun buttonsFunction() {
        val btnTrazarRuta = binding.btnTrazarRuta
        btnTrazarRuta.setOnClickListener {
            val intent = Intent(requireContext(),MapActivity::class.java)
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

}