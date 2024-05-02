package com.example.puctime.main

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.puctime.databinding.ActivityClockInFormBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


class ClockInFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClockInFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClockInFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkInTimer = binding.checkInTimeText
        val checkOutTimer = binding.checkOutTimeText

//        checkInTimer.setOnClickListener {
//
//            val calendar = Calendar.getInstance()
//            val timePickerDialog = TimePickerDialog(
//                /* context = */ this,
//                /* listener = */ { _, hourOfDay, minute ->
//                    calendar.apply {
//                        set(Calendar.HOUR_OF_DAY, hourOfDay)
//                        set(Calendar.MINUTE, minute)
//                    }
//                    val formattedTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
//                    checkInTimer.text = formattedTime
//                },
//                /* hourOfDay = */ calendar.get(Calendar.HOUR_OF_DAY),
//                /* minute = */ calendar.get(Calendar.MINUTE),
//                /* is24HourView = */ false
//            )
//            timePickerDialog.show()
//        }

    }
}

