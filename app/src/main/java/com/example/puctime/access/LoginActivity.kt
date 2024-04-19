package com.example.puctime.access

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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


        val resetPasswdLink = binding.resetPasswdLink
        resetPasswdLink.setOnClickListener{

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
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

    fun onButtonClick(view: View){
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
        view.startAnimation(scaleAnimation)
    }
}


