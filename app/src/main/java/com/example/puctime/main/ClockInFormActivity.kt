package com.example.puctime.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.example.puctime.R
import com.example.puctime.databinding.ActivityClockInFormBinding


class ClockInFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClockInFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockInFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultTimePicker = binding.defaultTimePicker
        val customTimePicker = binding.customTimePicker

        defaultTimePicker.setOnClickListener {
            // Default TimePicker
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setTitleColor(android.R.color.white)
            }.build().apply {
                setListener {
                        hour, minute ->
                    onTimePicked(hour, minute)
                }
            }.show(
                supportFragmentManager,
                SnapTimePickerDialog.TAG
            )
        }

        customTimePicker.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setTitleColor(android.R.color.black)
            }.build().apply {
                setListener {
                        hour, minute ->
                    onTimePicked(hour, minute)
                }
            }.show(
                supportFragmentManager,
                SnapTimePickerDialog.TAG
            )
        }

    }

    private fun onTimePicked(selectedHour: Int, selectedMinute: Int) {

        val selectedTime = binding.selectedTime

        val hour = selectedHour.toString()
            .padStart(2, '0')
        val minute = selectedMinute.toString()
            .padStart(2, '0')
        selectedTime.text = String.format(
            getString(
                R.string.selected_time_format,
                hour, minute
            )
        )
    }
}


