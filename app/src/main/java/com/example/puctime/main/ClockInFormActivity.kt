package com.example.puctime.main

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.example.puctime.R
import com.example.puctime.databinding.ActivityClockInFormBinding
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.model.Clockin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ClockInFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClockInFormBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockInFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference


        val checkInTime = binding.checkInTimeBox
        val checkOutTime = binding.checkOutTimeBox
        showPickedTimes(checkInTime, checkOutTime)

        val saveAppointmentButton = binding.saveClockInButton

        saveAppointmentButton.setOnClickListener {

            val checkInTimeText = binding.checkInSelectedTime.text.toString()
            val checkOutTimetext = binding.checkOutSelectedTime.text.toString()
            val clockInNametext = binding.clockInNameText.text.toString()
            val dayOfTheWeekSelectedText = binding.weekDaysActv.text.toString()

            if (clockInNametext.isEmpty()) {
                binding.clockInInputLayout.error = "Dê um nome à seu compromisso!"
            }

            if (dayOfTheWeekSelectedText.isEmpty()) {
                binding.weekDaysInputLayout.error = "Selecione um Dia da Semana!"
            }

            if (
                checkInTimeText.isNotEmpty() &&
                checkOutTimetext.isNotEmpty() &&
                clockInNametext.isNotEmpty() &&
                dayOfTheWeekSelectedText.isNotEmpty()
            ) {
                Log.d("clockInFormData", checkInTimeText)
                Log.d("clockInFormData", checkOutTimetext)
                Log.d("clockInFormData", clockInNametext)
                Log.d("clockInFormData", dayOfTheWeekSelectedText)

                registerNewClockin(
                    checkInTimeText,
                    checkOutTimetext,
                    clockInNametext,
                    dayOfTheWeekSelectedText
                )
            } else {
                Toast.makeText(this, "Erro ao criar apontamento", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        val weekDays = resources.getStringArray(R.array.week_days)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_week_days_item, weekDays)
        binding.weekDaysActv.setAdapter(arrayAdapter)
    }

    private fun showPickedTimes(
        checkInTime: LinearLayout,
        checkOutTime: LinearLayout
    ) {
        checkInTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setTitleColor(android.R.color.white)
            }.build().apply {
                setListener { hour, minute ->
                    onTimePickedForCheckIn(hour, minute)
                }
            }.show(
                supportFragmentManager,
                SnapTimePickerDialog.TAG
            )
        }

        checkOutTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setTitleColor(android.R.color.white)
            }.build().apply {
                setListener { hour, minute ->
                    onTimePickedForCheckOut(hour, minute)
                }
            }.show(
                supportFragmentManager,
                SnapTimePickerDialog.TAG
            )
        }
    }

    private fun onTimePickedForCheckIn(selectedHour: Int, selectedMinute: Int) {

        val checkInSelectedTime = binding.checkInSelectedTime
        val hour = selectedHour.toString()
            .padStart(2, '0')
        val minute = selectedMinute.toString()
            .padStart(2, '0')
        val selectedTime = String.format(
            getString(
                R.string.selected_time_format,
                hour, minute
            )
        )
        checkInSelectedTime.text = selectedTime
    }

    private fun onTimePickedForCheckOut(selectedHour: Int, selectedMinute: Int) {

        val checkOutSelectedTime = binding.checkOutSelectedTime
        val hour = selectedHour.toString()
            .padStart(2, '0')
        val minute = selectedMinute.toString()
            .padStart(2, '0')
        val selectedTime = String.format(
            getString(
                R.string.selected_time_format,
                hour, minute
            )
        )
        checkOutSelectedTime.text = selectedTime
    }

    private fun registerNewClockin(
        horarioInicio: String,
        horarioTermino: String,
        nome: String,
        diaDaSemana: String
    ) {

        userId = FirebaseMethods.returnUserId()

        if (userId == "null") {
            Toast.makeText(this, "Erro, usuário não encontrado", Toast.LENGTH_SHORT).show()
        } else {

            val clockInRef = database.child("users").child(userId).child("apontamentos").push()

            val clockIn = Clockin(horarioInicio, horarioTermino, nome, diaDaSemana)

            clockInRef.setValue(clockIn.toMap())
                .addOnSuccessListener {
                    Toast.makeText(this, "Apontamento criado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { error ->
                    Log.e("ClockInError", "Erro ao registrar novo apontamento: $error")
                }

        }
    }

}



