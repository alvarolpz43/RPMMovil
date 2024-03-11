package com.rpm.rpmmovil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rpm.rpmmovil.Login.Login
import com.rpm.rpmmovil.databinding.FragmentMenuBinding
import com.rpm.rpmmovil.profile.ViewProfile
import com.rpm.rpmmovil.utils.AppRPM
import com.rpm.rpmmovil.utils.Preferences

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

    //Traigo el token sisis
    val token= AppRPM.prefe.getToken()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            recuperarToken()
        }


        //Vamos al ViewProfile
        binding.userProfile.setOnClickListener {
            val intent:Intent= Intent(requireContext(), ViewProfile::class.java)
            startActivity(intent)
        }
    }





    private fun limpiarYRedirigirALogin() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(requireActivity(), Login::class.java)
        startActivity(intent)
        requireActivity().finish()
      //  val userTokenAfterClear = sharedPreferences.getString("token", null)
    //Toast.makeText(requireContext(), "Token después de limpiar: $userTokenAfterClear", Toast.LENGTH_SHORT).show()

    }

    private fun recuperarToken() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        val userToken = sharedPreferences.getString("token", null)
        Toast.makeText(requireContext(), "${userToken}", Toast.LENGTH_SHORT).show()

        if (userToken != null) {
            Toast.makeText(requireContext(),"Sesion Finalizada", Toast.LENGTH_SHORT).show()
            limpiarYRedirigirALogin()

        } else {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}
