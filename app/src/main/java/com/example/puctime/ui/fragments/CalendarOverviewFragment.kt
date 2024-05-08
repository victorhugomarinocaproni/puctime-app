package com.example.puctime.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.databinding.FragmentCalendarOverviewBinding
import com.example.puctime.ui.adapter.DailyClockInAdapter
import com.example.puctime.ui.interfaces.OnItemClickListener
import com.example.puctime.ui.main.ClockInFormActivity
import com.example.puctime.ui.models.Clockin
import com.example.puctime.ui.models.ClockinViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class CalendarOverviewFragment : Fragment() {

    private lateinit var viewModel: ClockinViewModel
    private var myAdapter: DailyClockInAdapter = DailyClockInAdapter()

    private var _binding: FragmentCalendarOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = binding.calendarFab

        viewModel = ViewModelProvider(this)[ClockinViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.daily_tasks_list)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = myAdapter

        viewModel.allClockin.observe(viewLifecycleOwner, Observer { clockinList ->
            myAdapter.setData(clockinList)
            myAdapter.setOnItemClickListener(object : OnItemClickListener{
                override fun onItemClick(clockin: Clockin) {
                    showBottomSheetDialog()
                }
            })
        })

        button.setOnClickListener {
            val intent = Intent(requireContext(), ClockInFormActivity::class.java)
            startActivity(intent)
        }

    }

    fun showBottomSheetDialog(){
        val sheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout,
            (ConstraintLayout(requireContext())).findViewById(R.id.bottom_sheet))
        sheetDialog.setContentView(view)
        sheetDialog.show()
    }

}


