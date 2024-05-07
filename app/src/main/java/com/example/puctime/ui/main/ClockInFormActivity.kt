package com.example.puctime.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.example.puctime.R
import com.example.puctime.databinding.ActivityClockInFormBinding
import com.example.puctime.infra.FirebaseMethods
import com.example.puctime.ui.models.Clockin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import java.time.Instant.now


class ClockInFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClockInFormBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var progressView: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockInFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.reference


        val checkInTime = binding.checkInTimeBox
        val checkOutTime = binding.checkOutTimeBox
        showPickedTimes(checkInTime, checkOutTime)

        val saveAppointmentButton = binding.saveClockInButton
        val backButton = binding.cancelButton

        progressView = binding.progressBar

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

        backButton.setOnClickListener {
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
            finish()
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

        progressView.visibility = View.VISIBLE

        userId = FirebaseMethods.returnUserId()

        if (userId == "null") {
            Toast.makeText(this, "Erro, usuário não encontrado", Toast.LENGTH_SHORT).show()
        } else {

            val clockInRef = database.child("users").child(userId).push()

            val dataCriacao = now()
            val dataCriacaoAponamento = Timestamp.from(dataCriacao).toString()

            val clockIn = Clockin(horarioInicio, horarioTermino, nome, diaDaSemana, dataCriacaoAponamento)

            clockInRef.setValue(clockIn.toMap())
                .addOnSuccessListener {
                    Toast.makeText(this, "Apontamento criado", Toast.LENGTH_SHORT).show()
                    progressView.visibility = View.GONE

                }
                .addOnFailureListener { error ->
                    Log.e("ClockInError", "Erro ao registrar novo apontamento: $error")
                }

        }
    }

}



