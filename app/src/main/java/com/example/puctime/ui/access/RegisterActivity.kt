package com.example.puctime.ui.access

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.R
import com.example.puctime.databinding.RegisterActivityBinding
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.ui.main.MainScreenActivity
import com.example.puctime.ui.models.User
import com.example.puctime.utils.Utils
import com.google.firebase.FirebaseApp

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.getInstance()
        Log.i("FirebaseConnection", "Conectado ao Firebase: ${(true)}")


        val registerButton = binding.materialButtonRegisterLayout

        registerButton.setOnClickListener {

            val userName = binding.textInputEditTextNameRegisterLayout.text.toString()
            val userEmail = binding.textInputEditTextEmailRegisterLayout.text.toString()
            val userWorkerId = binding.textInputEditTextWorkerIdRegisterLayout.text.toString()
            val userPasswd = binding.textInputEditTextPasswdRegisterRegisterLayout.text.toString()

            val user = User(userEmail, userPasswd, userName, userWorkerId)

            val flag = validateFormFields(userName, userEmail, userWorkerId, userPasswd)

            if (flag) {
                register(user)
            } else {
                Toast.makeText(
                    this,
                    "Preencha todos os campos para prosseguir",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun register(user: User) {
        FirebaseMethods.signUpUser(
            user.email,
            user.passwd,
            user.name,
            user.workerId
        ) { success, errorMessage ->
            if (success) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                FirebaseMethods.loginUser(
                    user.email,
                    user.passwd
                ) { loginSuccess, loginErrorMessage ->
                    if (loginSuccess) {
                        val intent = Intent(this, MainScreenActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        loginErrorMessage?.let {
                            Log.i("autoLoginFail", loginErrorMessage)
                            Toast.makeText(
                                this,
                                "Erro ao loggar automaticamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFormFields(
        name: String,
        email: String,
        workerId: String,
        passwd: String
    ): Boolean {
        if (name.isEmpty()) {
            binding.textInputEditTextNameRegisterLayout.error = "Preencha com seu nome"
        }

        if (email.isEmpty()) {
            binding.textInputEditTextEmailRegisterLayout.error = "Preencha com seu email"
        }

        if (workerId.isEmpty()) {
            binding.textInputEditTextWorkerIdRegisterLayout.error =
                "Preencha com sua Mátricula de Funcionário"
        }

        if (passwd.isEmpty()) {
            binding.textInputEditTextPasswdRegisterRegisterLayout.error = "Preencha com sua senha"
        }

        return name.isNotEmpty() && email.isNotEmpty() && workerId.isNotEmpty() && passwd.isNotEmpty()
    }

    fun onButtonClick(view: View) {
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
        view.startAnimation(scaleAnimation)
    }
}