package com.example.puctime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.databinding.FragmentClockInBinding
import com.example.puctime.models.Clockin
import com.example.puctime.ui.adapter.AllClockInAdapter
import com.example.puctime.ui.adapter.DailyClockInAdapter
import com.example.puctime.ui.interfaces.OnItemClickListener
import com.example.puctime.viewmodel.ClockinViewModel

class ClockInFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private var myAdapter: AllClockInAdapter? = null
    private lateinit var viewModel: ClockinViewModel

    private var _binding: FragmentClockInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        myAdapter = AllClockInAdapter()

        viewModel = ViewModelProvider(this)[ClockinViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.all_tasks_list)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = myAdapter

        progressBar.visibility = View.VISIBLE

        viewModel.allClockin.observe(viewLifecycleOwner) { clockinList ->
            val adapter = myAdapter
            adapter?.setData(clockinList)
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}