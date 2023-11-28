package com.example.doscaras


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(val items: List<Item>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<Adaptador.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imagen: ImageView = view.findViewById(R.id.cardImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun resetCards() {
        for (item in items) {
            item.girada = false
            item.resuelta = false
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (item.girada) {
            holder.imagen.setImageResource(item.imgid)
        } else {
            holder.imagen.setImageResource(R.drawable.cartitatata)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }
}