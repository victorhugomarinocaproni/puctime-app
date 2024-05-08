package com.example.puctime.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.puctime.R
import com.example.puctime.ui.interfaces.OnItemClickListener
import com.example.puctime.ui.models.Clockin

class DailyClockInAdapter : RecyclerView.Adapter<DailyClockInAdapter.CardViewHolder>() {

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

        Log.i("teste", "Item: $currentItem")

        holder.cardTitle.text = currentItem.nome
        holder.dayOfTheWeekText.text = currentItem.diaDaSemana
        holder.checkInText.text = currentItem.horarioInicio
        holder.checkOutText.text = currentItem.horarioTermino
        holder.itemView.setOnClickListener{ view ->

            Toast.makeText(view.context, "Hash: ${clockInList[position].id}", Toast.LENGTH_SHORT).show()
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
        clockInList.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.itemClickListener = listener
    }


}
