package com.example.puctime.ui.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.puctime.infra.repository.ClockinRepository

class ClockinViewModel : ViewModel() {

    private val repository: ClockinRepository = ClockinRepository().getInstance()
    val allClockin: LiveData<List<Clockin>> = repository.loadUserClockInData()
}