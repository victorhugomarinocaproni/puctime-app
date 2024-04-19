package com.example.puctime.infra

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.puctime.access.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseMethods {

    private val auth = FirebaseAuth.getInstance()

    fun signUpUser(
        email: String,
        passwd: String,
        nome: String,
        workerId: String,
        callback: (Boolean, String?) -> Unit
    ) {


        auth.createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val firestore = FirebaseFirestore.getInstance()
                    val user = auth.currentUser

                    user?.let {
                        val uid = it.uid
                        val userReference = firestore.collection("users").document(uid)
                        val userData = hashMapOf(
                            "nome" to nome,
                            "email" to email,
                            "workerId" to workerId
                        )

                        userReference.set(userData)
                            .addOnSuccessListener {
                                callback(true, null)
                            }
                            .addOnFailureListener { error ->
                                val errorMessage = "Erro ao salvar coleção: ${error.message}"
                                Log.i("collectionError", errorMessage)
                                callback(false, errorMessage)
                            }
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Erro no cadastro"
                    callback(false, errorMessage)
                }
            }
    }


    fun sendEmailResetPasswd(email: String, context: Context) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(context, "E-mail de recuperação enviado para: $email", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
            .addOnFailureListener {
                val error = it.message.toString()
                Toast.makeText(context, "E-mail inválido. Erro: $error", Toast.LENGTH_SHORT).show()
            }
    }

}