package com.rpm.rpmmovil

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rpm.rpmmovil.databinding.ActivityMainBinding
import com.rpm.rpmmovil.interfaces.ApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)



        home()
        initUi()

    }



    private fun initUi() {
        initNavigation()
    }

    private fun initNavigation() {

        val navHost= supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as  NavHostFragment
        navController= navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)


    }


    private fun home() {
        Toast.makeText(this, "Bienvenido Motero", Toast.LENGTH_SHORT).show()

    }



}