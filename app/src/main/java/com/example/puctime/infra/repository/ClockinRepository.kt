package com.example.puctime.infra.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.ui.models.Clockin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ClockinRepository {

    val uid = FirebaseMethods.returnUserId()

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val clockinRef: DatabaseReference = dbRef.child(uid)

    @Volatile private var INSTANCE : ClockinRepository ?= null


    // Garante que vai instanciar um repositório! Se tiver "null" cria um novo, senão só mantem

    fun getInstance() : ClockinRepository{
        return INSTANCE ?: synchronized(this) {

            val instance = ClockinRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadUserClockInData() : LiveData<List<Clockin>> {

        val clockinList = MutableLiveData<List<Clockin>>()

        clockinRef.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _clockinList : List<Clockin> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Clockin::class.java)!!
                    }

                    clockinList.postValue(_clockinList)

                } catch(e:Exception){
                    Log.e("error_loading_data", e.message.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return clockinList
    }
}