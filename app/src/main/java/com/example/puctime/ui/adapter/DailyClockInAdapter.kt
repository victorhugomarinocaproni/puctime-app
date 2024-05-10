package com.example.puctime.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.ui.interfaces.OnItemClickListener
import com.example.puctime.models.Clockin
import java.sql.Timestamp
import java.time.Instant
import java.time.Instant.now

class DailyClockInAdapter(private val context: Context) : RecyclerView.Adapter<DailyClockInAdapter.CardViewHolder>() {

    private val clockInList = ArrayList<Clockin>()
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_clock_in,
            parent,
            false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        val currentItem = clockInList[position]

        holder.cardTitle.text = currentItem.nome
        holder.dayOfTheWeekText.text = currentItem.diaDaSemana
        holder.checkInText.text = currentItem.horarioInicio
        holder.checkOutText.text = currentItem.horarioTermino

        val cardView = holder.itemView.findViewById<CardView>(R.id.card_view_item)

        if(currentItem.status == "aberto"){
            cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.opened_clockin_color))
            cardView.radius = context.resources.getDimensionPixelSize(R.dimen.card_view_radius).toFloat()
        }

        if(currentItem.status == "fechado"){
            cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.closed_clockin_color))
        }

        holder.itemView.setOnClickListener{ view ->
            itemClickListener?.onItemClick(currentItem)
        }
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
        val today = convertInstantTime()
        for(clockin in data){

            if(clockin.diaDaSemana == today){
                clockInList.add(clockin)
            }
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.itemClickListener = listener
    }

    private fun convertInstantTime() : String{

        val today = Timestamp.from(now()).toString()
        val day = today.substring(0, 3)

        if(day == "Mon") return "Segunda-feira"

        if(day == "Tue") return "Terça-feira"

        if(day == "Wed") return "Quarta-feira"

        if(day == "Thu") return "Quinta-feira"

        if(day == "Fri") return "Sexta-feira"

        return "Sábado"

    }



}
