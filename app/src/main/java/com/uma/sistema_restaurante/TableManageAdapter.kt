package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TableManageAdapter(
    private var mesas: List<Mesa>,
    private val onEdit: (Mesa) -> Unit,
    private val onDelete: (Mesa) -> Unit
) : RecyclerView.Adapter<TableManageAdapter.TableViewHolder>() {

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMesaId: TextView = view.findViewById(R.id.tvMesaNumero)
        val tvCapacidad: TextView = view.findViewById(R.id.tvMesaCapacidad)
        val tvEstado: TextView = view.findViewById(R.id.tvMesaEstado)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditMesa)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteMesa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.tvMesaId.text = "Mesa ${mesa.id}"
        holder.tvCapacidad.text = "Capacidad: ${mesa.capacidad}"
        holder.tvEstado.text = "Estado: ${mesa.estado}"

        // Colores según el estado
        when (mesa.estado.lowercase()) {
            "libre" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            "reservada" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_orange_dark))
            else -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
        }

        holder.btnEdit.setOnClickListener { onEdit(mesa) }
        holder.btnDelete.setOnClickListener { onDelete(mesa) }
    }

    override fun getItemCount() = mesas.size

    fun updateList(newList: List<Mesa>) {
        mesas = newList
        notifyDataSetChanged()
    }
}
