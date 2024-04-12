package com.example.puctime.access

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.R
import com.example.puctime.databinding.RegisterActivityBinding
import com.example.puctime.utils.Utils

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val registerButton = binding.materialButtonRegisterLayout

        registerButton.setOnClickListener {

            val userName = binding.textInputEditTextNameRegisterLayout.text.toString()
            val userEmail = binding.textInputEditTextEmailRegisterLayout.text.toString()
            val userWorkerId = binding.textInputEditTextWorkerIdRegisterLayout.text.toString()
            val userPasswd = binding.textInputEditTextPasswdRegisterRegisterLayout.text.toString()

            Log.i("registerLayoutData", userName)
            Log.i("registerLayoutData", userEmail)
            Log.i("registerLayoutData", userWorkerId)
            Log.i("registerLayoutData", userPasswd)

        }


        Utils.underlineText(
            this,
            binding.linkToLoginScreenRegisterLayout,
            R.color.dark_blue,
            19
        )

        Utils.setLinkClickable(
            this,
            binding.linkToLoginScreenRegisterLayout,
            19,
            LoginActivity::class.java
        )

    }





}