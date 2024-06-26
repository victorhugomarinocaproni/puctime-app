package com.example.puctime.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.databinding.FragmentCalendarOverviewBinding
import com.example.puctime.ui.adapter.DailyClockInAdapter
import com.example.puctime.ui.interfaces.OnItemClickListener
import com.example.puctime.ui.main.ClockInFormActivity
import com.example.puctime.models.Clockin
import com.example.puctime.viewmodel.ClockinViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class CalendarOverviewFragment : Fragment() {

    private lateinit var viewModel: ClockinViewModel
    private var myAdapter: DailyClockInAdapter? = null
    private var sheetDialog: BottomSheetDialog? = null
    private lateinit var progressView: ProgressBar

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

        myAdapter = DailyClockInAdapter(requireContext())

        progressView = binding.progressBar

        val createNewClockinButton = binding.calendarFab

        viewModel = ViewModelProvider(this)[ClockinViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.daily_tasks_list)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = myAdapter

        progressView.visibility = View.VISIBLE

        viewModel.allClockin.observe(viewLifecycleOwner) { clockinList ->
            val adapter = myAdapter
            adapter?.setData(clockinList)
            progressView.visibility = View.GONE
            adapter?.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(clockin: Clockin) {
                    showBottomSheetDialog(clockin)
                }
            })
        }

        createNewClockinButton.setOnClickListener {
            val intent = Intent(requireContext(), ClockInFormActivity::class.java)
            startActivity(intent)
        }
    }

    fun showBottomSheetDialog(clockin: Clockin) {

        sheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)

        val view: View

        when (clockin.status) {
            "" -> {

                view = LayoutInflater.from(requireContext()).inflate(
                    R.layout.bottom_sheet_layout_clockin,
                    (ConstraintLayout(requireContext())).findViewById(R.id.bottom_sheet_clockin))

                val clockInButton = view.findViewById<MaterialButton>(R.id.register_clockin_button)
                clockInButton.setOnClickListener {
                    progressView.visibility = View.VISIBLE
                    registerClockIn(clockin)
                }

            }
            "aberto" -> {
                view = LayoutInflater.from(requireContext()).inflate(
                    R.layout.bottom_sheet_layout_clockout,
                    (ConstraintLayout(requireContext())).findViewById(R.id.bottom_sheet_clockout))

                val clockOutButton = view.findViewById<MaterialButton>(R.id.register_clockout_button)
                clockOutButton.setOnClickListener {
                    registerClockOut(clockin)
                }
            }
            else -> {
                view = LayoutInflater.from(requireContext()).inflate(
                    R.layout.bottom_sheet_layout_already_registered,
                    (ConstraintLayout(requireContext())).findViewById(R.id.bottom_sheet_already_registered))
            }
        }

        sheetDialog?.setContentView(view)
        sheetDialog?.show()
    }

    private fun registerClockIn(clockin: Clockin) {
        val repositoryAnswer = viewModel.saveClockInRegister(clockin)

        Log.i("RegistroClockin", "Fragment: $repositoryAnswer")

        when (repositoryAnswer) {
            "true" -> {
                progressView.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Entrada registrada com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                dismissBottomSheetDialog()
            }

            "Fora de horário" -> {
                progressView.visibility = View.GONE
                Toast.makeText(requireContext(), "Fora de Horário", Toast.LENGTH_SHORT).show()
            }

            else -> {
                progressView.visibility = View.GONE
                Toast.makeText(requireContext(), "Erro ao registrada entrada!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun registerClockOut(clockin: Clockin){

        val repositoryAnswer = viewModel.saveClockOutRegister(clockin)

        Log.i("RegistroClockin", "ClockOut repo answer: $repositoryAnswer")

        when (repositoryAnswer) {
            "true" -> {
                progressView.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Saida registrada com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                dismissBottomSheetDialog()
            }

            else -> {
                progressView.visibility = View.GONE
                Toast.makeText(requireContext(), "Fora de Horário!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun dismissBottomSheetDialog() {
        sheetDialog?.dismiss()
    }
}


