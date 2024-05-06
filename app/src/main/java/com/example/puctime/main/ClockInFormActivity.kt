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


class ClockInFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClockInFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockInFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                Toast.makeText(this, "Apontamento criado", Toast.LENGTH_SHORT).show()
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
}


