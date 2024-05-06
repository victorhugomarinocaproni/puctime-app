package com.example.puctime.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.puctime.databinding.FragmentCalendarOverviewBinding
import com.example.puctime.main.ClockInFormActivity

class CalendarOverviewFragment : Fragment() {


    private var _binding: FragmentCalendarOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.calendarFab
        button.setOnClickListener {
            val intent = Intent(requireContext(), ClockInFormActivity::class.java)
            startActivity(intent)
        }
    }
}