package com.example.puctime.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.puctime.infra.repository.ClockinRepository
import com.example.puctime.ui.models.Clockin

class ClockinViewModel : ViewModel() {

    private val repository: ClockinRepository = ClockinRepository().getInstance()
    val allClockin: LiveData<List<Clockin>> = repository.loadUserClockInData()
    val allHashCodes: LiveData<List<String>> = repository.getAllClockinHashCodes()

    fun saveClockInRegister(clockin : Clockin) : String{
        val answer = repository.saveClockinRegister(clockin)
        Log.i("RegistroClockin", answer)
        return answer
    }
}