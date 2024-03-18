package com.rpm.rpmmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rpm.rpmmovil.Login.Login
import com.rpm.rpmmovil.databinding.FragmentMenuBinding
import com.rpm.rpmmovil.interfaces.ApiClient
import com.rpm.rpmmovil.interfaces.ApiServices
import com.rpm.rpmmovil.profile.ViewProfile
import com.rpm.rpmmovil.profile.model.dataProfileUser
import com.rpm.rpmmovil.utils.AppRPM
import com.rpm.rpmmovil.utils.Preferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    //Traemos lod Datos necesarios
    val retrofit = ApiClient.web
    val token = AppRPM.prefe.getToken()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogout.setOnClickListener {
            limpiarYRedirigirALogin()
        }
        llenarDatos()

        //Vamos al ViewProfile
        binding.userProfile.setOnClickListener {
            val intent: Intent = Intent(requireContext(), ViewProfile::class.java)
            startActivity(intent)
        }


    }

    private fun llenarDatos() {
        val token = AppRPM.prefe.getToken().toString()

        lifecycleScope.launch(Dispatchers.IO) {
            val response: Response<dataProfileUser> = retrofit.getprofileUser(token)
            if (response.isSuccessful) {
                val myResponse = response.body()
                withContext(Dispatchers.Main) {
                    myResponse?.let {
                        binding.userName.setText(myResponse.userFound.Nombres_Mv)
                        binding.userEmail.setText(myResponse.userFound.Email_Mv)
                        if (myResponse.userFound.ImageUser.isEmpty()) {
                            Picasso.get()
                                .load("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png")
                                .into(binding.userPhoto)

                        } else {
                            Picasso.get()
                                .load(myResponse.userFound.ImageUser)
                                .into(binding.userPhoto)

                        }


                    }


                }

            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Fallo al obtener los datos por q?? nose",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }
    }


    private fun limpiarYRedirigirALogin() {
        AppRPM.prefe.clearPreferences()
        val intent = Intent(requireContext(), Login::class.java)
        startActivity(intent)
        requireActivity().finish()
    }




}
