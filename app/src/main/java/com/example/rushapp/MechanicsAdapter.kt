package com.example.rushapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MechanicsAdapter(private val mechanics: List<Mechanic>) :
    RecyclerView.Adapter<MechanicsAdapter.MechanicViewHolder>() {

    class MechanicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.mechanicNameTextView)
        val emailTextView: TextView = view.findViewById(R.id.mechanicEmailTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MechanicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mechanic, parent, false)
        return MechanicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MechanicViewHolder, position: Int) {
        val mechanic = mechanics[position]
        holder.nameTextView.text = mechanic.name
        holder.emailTextView.text = mechanic.email
    }

    override fun getItemCount(): Int = mechanics.size
}
