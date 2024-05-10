package com.example.puctime.infra.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.models.Clockin
import com.example.puctime.models.ClockinRegister
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.values
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.time.Instant.now
import java.util.Calendar

class ClockinRepository {

    val uid = FirebaseMethods.returnUserId()

    private val getMethodDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    private val clockinRef: DatabaseReference = getMethodDbRef.child(uid)

    private val postMethodRegisterRef: DatabaseReference =
        Firebase.database.reference.child("registers").child(uid)


    // Garante que vai instanciar um repositório! Se tiver "null" cria um novo, senão só mantem
    @Volatile
    private var INSTANCE: ClockinRepository? = null

    fun getInstance(): ClockinRepository {
        return INSTANCE ?: synchronized(this) {

            val instance = ClockinRepository()
            INSTANCE = instance
            instance
        }
    }

//==========================================================================================

    fun loadUserClockInData(): LiveData<List<Clockin>> {

        val clockinList = MutableLiveData<List<Clockin>>()

        clockinRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _clockinList: List<Clockin> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Clockin::class.java)!!
                    }

                    clockinList.postValue(_clockinList)

                } catch (e: Exception) {
                    Log.e("error_loading_data", e.message.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return clockinList
    }

    fun getAllClockinHashCodes(): LiveData<List<String>> {

        val hashCodesList = MutableLiveData<List<String>>()

        clockinRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    val _hashCodesList: List<String> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.key.toString()
                    }
                    hashCodesList.postValue(_hashCodesList)
                } catch (e: Exception) {
                    Log.e("error_loading_hashCodes", e.message.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return hashCodesList
    }

    fun saveClockinRegister(clockin: Clockin): String {

        val canRegister: Boolean = validateTimeClockIn(clockin)

        if (canRegister) {

            val saveClockInRef = postMethodRegisterRef.push()
            val registeredClockinTimestampInString = Timestamp.from(now()).toString()

            val clockIn =
                ClockinRegister(
                    nome = clockin.nome,
                    diaDaSemana = clockin.diaDaSemana,
                    entrada = registeredClockinTimestampInString,
                    saida = "",
                    idTaskReferencia = clockin.id
                )

            saveClockInRef.setValue(clockIn.toMap()).addOnSuccessListener {
                Log.i("RegistroClockin", "Entrada registrada com sucesso!")
                setClockinStatusToOpened(clockin.id)
            }.addOnFailureListener { error ->
                Log.i("RegistroClockin", "Erro ao registrar entrada ${error}")
            }
            return "true"
        } else {
            return "Fora de horário"
        }
    }

    fun saveClockOutRegister(clockin: Clockin): String {

        val canRegister: Boolean = validateTimeClockOut(clockin)
        val registeredClockOutTimestampInString = Timestamp.from(now()).toString()

        if (canRegister) {

            postMethodRegisterRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {

                        val clockInId = snapshot.child("idTaskReferencia").value.toString()

                        if (clockInId == clockin.id) {

                            val clockInData = snapshot.getValue(ClockinRegister::class.java)

                            if (clockInData != null) {
                                snapshot.child("saida").ref.setValue(
                                    registeredClockOutTimestampInString
                                )
                                    .addOnSuccessListener {
                                        Log.i("RegistroClockin", "Saida registrada com sucesso")
                                        setClockinStatusToClosed(
                                            clockin.id
                                        )
                                    }
                                    .addOnFailureListener {
                                        Log.i("RegistroClockin", "Erro ao registrar saida")
                                    }
                            }
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "RegistroClockin",
                        "Erro ao ler dados e registrar saida: ${error.message}"
                    )
                }
            })
            return "true"
        } else {
            return "Fora de Horário"
        }
    }

    private fun validateTimeClockIn(clockin: Clockin): Boolean {

        val now = getInstantTime()
        val clockIn = getClockInTime(clockin)
        val arrivalTimeWithTollerance = getTimeWithTollerance(clockIn)

        return now in clockIn..arrivalTimeWithTollerance
    }

    private fun validateTimeClockOut(clockin: Clockin): Boolean {

        val now = getInstantTime()
        val clockOut = getClockOutTime(clockin)
        val exitTimeWithTollerance = getTimeWithTollerance(clockOut)

        return now in clockOut..exitTimeWithTollerance

    }

    private fun getInstantTime(): Timestamp {
        // Thu May 09 13:52:15 GMT-03:00 2024
        // hour: startIndex = 11 - endIndex = 13 (end is excluded)
        // min: startIndex = 14 - endIndex = 16 (end is excluded)
        // year: startIndex = 30 ou (melhor) length - 4

        val calendar = Calendar.getInstance()

        val now = Timestamp.from(now()).toString()
        val length = now.length

        val hour = now.substring(11, 13).toInt()
        val minute = now.substring(14, 16).toInt()
        val year = now.substring(length - 4).toInt()

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
        val length = clockin.dataCriacaoAponamento.length


        val clockInHour = clockin.horarioInicio.substring(0, 2).toInt()
        val clockInMinute = clockin.horarioInicio.substring(3).toInt()
        val year = clockin.dataCriacaoAponamento.substring(length - 4).toInt()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, clockInHour)
        calendar.set(Calendar.MINUTE, clockInMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return Timestamp(calendar.timeInMillis)

    }

    private fun getClockOutTime(clockin: Clockin): Timestamp {

        val calendar = Calendar.getInstance()
        val length = clockin.dataCriacaoAponamento.length

        val clockOutHour = clockin.horarioTermino.substring(0, 2).toInt()
        val clockOutMinute = clockin.horarioTermino.substring(3).toInt()
        val year = clockin.dataCriacaoAponamento.substring(length - 4).toInt()

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, clockOutHour)
        calendar.set(Calendar.MINUTE, clockOutMinute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return Timestamp(calendar.timeInMillis)

    }

    private fun getTimeWithTollerance(originalTime: Timestamp): Timestamp {

        val tolleranceInMinutes: Long = 10
        val tolleranceInMillis: Long = tolleranceInMinutes * 60 * 1000
        val originalTimeLongType: Long = originalTime.time

        val timeWithTollerance = originalTimeLongType + tolleranceInMillis

        return Timestamp(timeWithTollerance)

    }

    private fun setClockinStatusToOpened(id: String) {

        clockinRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {

                    val clockInId = snapshot.child("id").value.toString()

                    if (clockInId == id) {

                        val clockInData = snapshot.getValue(Clockin::class.java)
                        if (clockInData != null) {
                            snapshot.child("status").ref.setValue("aberto")
                                .addOnSuccessListener {
                                    Log.i("RegistroClockin", "Status atualizado para 'aberto'")
                                }.addOnFailureListener {
                                    Log.i(
                                        "RegistroClockin",
                                        "Erro ao atualizar status para 'aberto'"
                                    )

                                }
                        }
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "RegistroClockin",
                    "Erro ao ler dados atualizando status para 'aberto': ${error.message}"
                )
            }
        })
    }

    private fun setClockinStatusToClosed(id: String) {

        clockinRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children) {
                    val snapshotId = snapshot.child("id").value.toString()
                    if(snapshotId == id){
                        snapshot.child("status").ref.setValue("fechado")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("RegistroClockin", "Erro ao atualizar status para fechado: $error")
            }

        })


//        clockinRef.child(hash).child("status").setValue("fechado")
//            .addOnSuccessListener {
//                Log.i("RegistroClockin", "Status atualizado para 'fechado'")
//            }
//            .addOnFailureListener {
//                Log.i("RegistroClockin", "Erro ao atualizar status para 'fechado'")
//            }
    }
}


