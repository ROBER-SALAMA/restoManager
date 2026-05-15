package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MesaAdapter(
    private val mesas: List<Mesa>,
    private val onMesaClick: (Mesa) -> Unit
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    class MesaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardMesa: CardView = view.findViewById(R.id.cardMesa)
        val tvMesaNumero: TextView = view.findViewById(R.id.tvMesaNumero)
        val tvMesaCapacidad: TextView = view.findViewById(R.id.tvMesaCapacidad)
        val tvMesaEstado: TextView = view.findViewById(R.id.tvMesaEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
        return MesaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.tvMesaNumero.text = "Mesa ${mesa.id}"
        holder.tvMesaCapacidad.text = "Capacidad: ${mesa.capacidad}"
        holder.tvMesaEstado.text = mesa.estado.replaceFirstChar { it.uppercase() }
        
        when (mesa.estado) {
            "libre" -> {
                holder.tvMesaEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            }
            "reservada" -> {
                holder.tvMesaEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_orange_dark))
            }
            "facturada" -> {
                holder.tvMesaEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_blue_dark))
            }
        }

        // Permitimos clic en cualquier estado para gestionar o agregar productos
        holder.cardMesa.setOnClickListener { onMesaClick(mesa) }
    }

    override fun getItemCount() = mesas.size
}
