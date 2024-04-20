package com.example.puctime.access

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.databinding.ForgotPasswordActivityBinding
import com.example.puctime.infra.FirebaseMethods

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ForgotPasswordActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ForgotPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resetPasswdButton = binding.materialButtonForgotPasswdLayout

        resetPasswdButton.setOnClickListener {

            val emailField = binding.textInputEditTextEmailForgotPasswdLayout
            val userEmail = emailField.text.toString().trim()

            if(userEmail.isEmpty()){
                emailField.error = "Este campo não poder estar vazio"
            } else {
                resetPassword(userEmail)
            }
        }
    }

    private fun resetPassword(email: String){
        FirebaseMethods.sendEmailResetPasswd(email) { success, errorMessage ->
            if(success){
                Toast.makeText(this, "E-mail de alteração de senha enviado para: $email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                if(errorMessage != null){
                    Log.i("resetPasswdEmailFail", errorMessage)
                }
                Toast.makeText(this, "Falha ap enviar e-mail de alteração de senha", Toast.LENGTH_SHORT).show()

            }
        }
    }


}