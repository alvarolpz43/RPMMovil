package com.rpm.rpmmovil

import com.rpm.rpmmovil.Rmotos.GarageActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment



import com.rpm.rpmmovil.ExplorarRutas.ExploraRutasActivity

import com.rpm.rpmmovil.Routes.ListarRutasActivity
import com.rpm.rpmmovil.Routes.MapActivity
import com.rpm.rpmmovil.databinding.FragmentHomeBinding


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
           val intent = Intent(requireContext(),ExploraRutasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
