package com.example.puctime.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.access.LoginActivity
import com.example.puctime.databinding.MainScreenActivityBinding
import com.example.puctime.infra.FirebaseMethods

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding : MainScreenActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val backButton = binding.backButtonMainScreen
        backButton.setOnClickListener{

            signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun signOut() {
        FirebaseMethods.signOutUser()
    }
}