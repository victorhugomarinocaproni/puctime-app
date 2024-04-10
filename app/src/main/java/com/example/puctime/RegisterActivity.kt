package com.example.puctime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.databinding.RegisterActivityBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}