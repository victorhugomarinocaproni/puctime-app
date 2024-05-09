package com.example.puctime.infra.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.ui.models.Clockin
import com.example.puctime.ui.models.ClockinRegister
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.time.Instant.now
import java.util.Calendar

class ClockinRepository {

    val uid = FirebaseMethods.returnUserId()

    private val getMethodDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private val clockinRef: DatabaseReference = getMethodDbRef.child(uid)
    private val postMethodDbRef: DatabaseReference = Firebase.database.reference.child("registers").child(uid)

    // Garante que vai instanciar um repositório! Se tiver "null" cria um novo, senão só mantem

    @Volatile private var INSTANCE : ClockinRepository ?= null

    fun getInstance() : ClockinRepository{
        return INSTANCE ?: synchronized(this) {

            val instance = ClockinRepository()
            INSTANCE = instance
            instance
        }
    }

//==========================================================================================

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

    fun getAllClockinHashCodes() : LiveData<List<String>>{

        val hashCodesList = MutableLiveData<List<String>>()

        clockinRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _hashCodesList: List<String> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.key.toString()
                    }
                    hashCodesList.postValue(_hashCodesList)
                } catch (e : Exception){
                    Log.e("error_loading_hashCodes", e.message.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return hashCodesList
    }

    fun saveClockinRegister(clockin : Clockin) : String{

        if(validateTimeClockIn(clockin)){

            var result = ""

            val saveClockInRef = postMethodDbRef.push()
            val instantClockinRegistered = now()
            val registeredClockinTimestamp = Timestamp.from(instantClockinRegistered).toString()

            val clockIn = ClockinRegister(clockin.nome, clockin.diaDaSemana, registeredClockinTimestamp)

            saveClockInRef.setValue(clockIn.toMap())
                .addOnSuccessListener {
                    result = "true"
                    Log.i("RegistroClockin", "Ponto batido e registrado com sucesso!")
                }
                .addOnFailureListener { error ->
                    result = "registerClockinFail"
                    Log.i("RegistroClockin", "Erro ao registrar o ponto batido ${error}")
                }

            return result

        } else {
            return "Fora de horário"
        }
    }

    private fun validateTimeClockIn(clockin: Clockin) : Boolean{

        val now = getInstantTime()
        val clockIn = getClockInTime(clockin)
        val arrivalTimeWithTollerance = getArrivalTimeWithTolerance(clockIn)

        return now in clockIn..arrivalTimeWithTollerance
        //now >= clockIn && now<= arrivalTimeWithTollerance

    }

    private fun getInstantTime() : Timestamp{
        //Thu May 09   1   3  : 2   0  :  2  4 spc G  M  T spc 2 0 2 4
        //012345678910 11 12 13 14 15 16 17 18 19 20 21 22 23  24 25

        val calendar = Calendar.getInstance()

        val now = Timestamp.from(now()).toString()
        val hour = now.substring(11, 13).toInt()
        val minute = now.substring(14, 16).toInt()
        val year = now.substring(24).toInt()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val timestampInMillis = calendar.timeInMillis

        return Timestamp(timestampInMillis)

    }

    private fun getClockInTime(clockin: Clockin): Timestamp {

        val calendar = Calendar.getInstance()

        val clockInHour = clockin.horarioInicio.substring(0, 2).toInt()
        val clockInMinute = clockin.horarioInicio.substring(3).toInt()
        val year = clockin.dataCriacaoAponamento.substring(24).toInt()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, clockInHour)
        calendar.set(Calendar.MINUTE, clockInMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return Timestamp(calendar.timeInMillis)

    }

    private fun getClockOutTime(clockin: Clockin): Timestamp {

        val calendar = Calendar.getInstance()

        val clockOutHour = clockin.horarioTermino.substring(0, 2).toInt()
        val clockOutMinute = clockin.horarioTermino.substring(3).toInt()
        val year = clockin.dataCriacaoAponamento.substring(24).toInt()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, clockOutHour)
        calendar.set(Calendar.MINUTE, clockOutMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return Timestamp(calendar.timeInMillis)

    }

    private fun getArrivalTimeWithTolerance(arrivalTime: Timestamp) : Timestamp{

        val tolleranceInMinutes: Long = 10
        val tolleranceInMillis: Long = tolleranceInMinutes * 60 * 1000
        val arrivalTimeLong: Long = arrivalTime.time

        val arrivalTimeWithToleranceLong = arrivalTimeLong + tolleranceInMillis

        return Timestamp(arrivalTimeWithToleranceLong)

    }
}