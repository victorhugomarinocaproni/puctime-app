package com.example.puctime

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.databinding.RegisterActivityBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val registerButton = binding.materialButtonRegisterLayout

        registerButton.setOnClickListener{

            val nameText = binding.textInputEditTextNameRegisterLayout.text.toString()
            val emailText = binding.textInputEditTextEmailRegisterLayout.text.toString()
            val workerIdText = binding.textInputEditTextWorkerIdRegisterLayout.text.toString()
            val passwdText = binding.textInputEditTextPasswdRegisterRegisterLayout.text.toString()

            Log.i("registerLayoutData", nameText)
            Log.i("registerLayoutData", emailText)
            Log.i("registerLayoutData", workerIdText)
            Log.i("registerLayoutData", passwdText)

        }
    }
}