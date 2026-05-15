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
        val btnEdit: ImageButton = ImageButton(view.context) // Temporary creation if not in layout, but let's assume we use a specific layout or reuse
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.tvMesaId.text = "Mesa ${mesa.id}"
        holder.tvCapacidad.text = "Capacidad: ${mesa.capacidad}"
        holder.tvEstado.text = mesa.estado

        // For management, we might want to add edit/delete buttons to item_mesa or use a different layout.
        // Let's modify item_mesa.xml to include management buttons or use long click.
        // To follow the "modern and functional" request, let's use a specific management item layout.
        
        holder.itemView.setOnClickListener { onEdit(mesa) }
        holder.itemView.setOnLongClickListener {
            onDelete(mesa)
            true
        }
    }

    override fun getItemCount() = mesas.size

    fun updateList(newList: List<Mesa>) {
        mesas = newList
        notifyDataSetChanged()
    }
}
