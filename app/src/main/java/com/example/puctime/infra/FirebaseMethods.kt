package com.example.puctime.infra

import android.content.Context
import android.util.Log
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


    fun sendEmailResetPasswd(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    callback(true, null)
                } else {
                    val errorMessage = task.exception?.message
                    callback(false, errorMessage)
                }
            }
    }

    fun loginUser(email: String, passwd: String, callback: (Boolean, String?) -> Unit) {

        auth.signInWithEmailAndPassword(email, passwd)
            .addOnCompleteListener { task ->

                if(task.isSuccessful){
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun getUserConnection(callback: (Boolean) -> Unit) {
        if(auth.currentUser != null){
            callback(true)
        } else {
            callback(false)
        }
    }

    fun signOutUser() {
        auth.signOut()
    }
}