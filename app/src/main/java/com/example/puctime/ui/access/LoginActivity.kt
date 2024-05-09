package com.example.puctime.ui.access

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.puctime.R
import com.example.puctime.databinding.LoginActivityBinding
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.ui.main.MainScreenActivity
import com.example.puctime.utils.Utils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)
        installSplashScreen()

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.materialButtonLoginLayout
        val emailField = binding.textInputEditTextEmailLoginLayout
        val passwdField = binding.textInputEditTextPasswdLoginLayout

        loginButton.setOnClickListener {

            val userEmail = emailField.text.toString().trim()
            val userPasswd = passwdField.text.toString().trim()

            if (userEmail.isEmpty()) {
                emailField.error = "O campo 'e-mail' deve estar preenchido"
            }

            if (userPasswd.isEmpty()) {
                passwdField.error = "O campo 'senha' deve estar preenchido"
            }

            if (userEmail.isNotEmpty() && userPasswd.isNotEmpty()) {
                login(userEmail, userPasswd, this)
            }
        }


        val resetPasswdLink = binding.resetPasswdLink
        resetPasswdLink.setOnClickListener {

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

        Utils.underlineText(
            this,
            binding.resetPasswdLink,
            R.color.dark_blue,
            0
        )

    }

    override fun onStart() {
        super.onStart()

        FirebaseMethods.getUserConnection{ connected ->
            if(connected){
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun login(email: String, passwd: String, context: Context) {
        FirebaseMethods.loginUser(email, passwd){ success, errorMessage ->

            if(success){
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                errorMessage?.let {
                    Log.i("loginFail", errorMessage)
                    Toast.makeText(this, "Falha ao realizar login", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onButtonClick(view: View) {
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
        view.startAnimation(scaleAnimation)
    }


}





