package com.rpm.rpmmovil.Rmotos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rpm.rpmmovil.databinding.ActivityShowGarageBinding

class ShowGarageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShowGarageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowGarageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}