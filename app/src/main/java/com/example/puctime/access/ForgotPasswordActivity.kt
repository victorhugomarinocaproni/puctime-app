package com.example.puctime.access

import android.app.Activity
import android.content.Context
import android.os.Bundle
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
                ResetPassword(userEmail, this)
            }
        }
    }


    private fun ResetPassword(email: String, context: Context){
        FirebaseMethods.sendEmailResetPasswd(email, context)
        finish()
    }


}