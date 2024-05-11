package com.example.puctime.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.models.Clockin
import java.sql.Timestamp
import java.time.Instant

class AllClockInAdapter : RecyclerView.Adapter<AllClockInAdapter.CardViewHolder>() {

    private val clockInList = ArrayList<Clockin>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_clock_in,
            parent,
            false
        )
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = clockInList[position]

        holder.cardTitle.text = currentItem.nome
        holder.dayOfTheWeekText.text = currentItem.diaDaSemana
        holder.checkInText.text = currentItem.horarioInicio
        holder.checkOutText.text = currentItem.horarioTermino

    }

    override fun getItemCount(): Int {
        return clockInList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cardTitle: TextView = itemView.findViewById(R.id.clock_in_card_title)
        val dayOfTheWeekText: TextView = itemView.findViewById(R.id.day_of_the_week_card_text)
        val checkInText: TextView = itemView.findViewById(R.id.check_in_card_selected_time)
        val checkOutText: TextView = itemView.findViewById(R.id.check_out_card_selected_time)
    }

    fun setData(data: List<Clockin>){
        clockInList.clear()
        clockInList.addAll(data)
        notifyDataSetChanged()
    }

    private fun convertInstantTime() : String{

        val today = Timestamp.from(Instant.now()).toString()
        val day = today.substring(0, 3)

        if(day == "Mon") return "Segunda-feira"

        if(day == "Tue") return "Terça-feira"

        if(day == "Wed") return "Quarta-feira"

        if(day == "Thu") return "Quinta-feira"

        if(day == "Fri") return "Sexta-feira"

        return "Sábado"

    }
}