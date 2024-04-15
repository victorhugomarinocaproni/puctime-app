package com.example.puctime.infra

import com.google.firebase.auth.FirebaseAuth

class FirebaseMethods {

    companion object {

        private val auth = FirebaseAuth.getInstance()

        fun signUpUser(email: String, passwd: String, callback: (Boolean, String?) -> Unit) {

            auth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true, null)
                    } else {
                        val errorMessage = task.exception?.message ?: "Erro no cadastro"
                        callback(false, errorMessage)
                    }
                }
        }
    }

}