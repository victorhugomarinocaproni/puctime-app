package com.example.puctime.access

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.puctime.R
import com.example.puctime.databinding.LoginActivityBinding
import com.example.puctime.utils.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val loginButton = binding.materialButtonLoginLayout

        loginButton.setOnClickListener {

            val userEmail = binding.textInputEditTextEmailLoginLayout.text.toString()
            val userPasswd = binding.textInputEditTextPasswdLoginLayout.text.toString()

        }

        Utils.underlineText(
            this,
            binding.linkToRegisterScreenLoginLayout,
            R.color.dark_blue,
            19
        )

        Utils.setLinkClickable(
            this,
            binding.linkToRegisterScreenLoginLayout,
            19,
            RegisterActivity::class.java
        )


    }


}


