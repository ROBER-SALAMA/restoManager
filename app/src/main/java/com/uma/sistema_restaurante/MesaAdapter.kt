package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para mostrar la cuadrícula de mesas en la pantalla principal.
 * Cambia visualmente el estado de la mesa dependiendo de su disponibilidad.
 */
class MesaAdapter(
    private val mesas: List<Mesa>,
    private val onMesaClick: (Mesa) -> Unit
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    /**
     * ViewHolder que mantiene las referencias a los elementos visuales de una mesa.
     */
    class MesaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardMesa: CardView = view.findViewById(R.id.cardMesa)
        val tvMesaNumero: TextView = view.findViewById(R.id.tvMesaNumero)
        val tvMesaCapacidad: TextView = view.findViewById(R.id.tvMesaCapacidad)
        val tvMesaEstado: TextView = view.findViewById(R.id.tvMesaEstado)
    }

    /**
     * Crea la vista para cada ítem de mesa.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
        return MesaViewHolder(view)
    }

    /**
     * Asocia los datos de la mesa con la interfaz y define el comportamiento al hacer clic.
     */
    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.tvMesaNumero.text = "Mesa ${mesa.id}"
        holder.tvMesaCapacidad.text = "Capacidad: ${mesa.capacidad}"
        
        // Lógica visual según disponibilidad
        if (mesa.estaDisponible) {
            holder.tvMesaEstado.text = "Disponible"
            // Color verde para indicar que se puede reservar
            holder.tvMesaEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_green_dark))
            // Solo permitimos clics si la mesa está disponible
            holder.cardMesa.setOnClickListener { onMesaClick(mesa) }
        } else {
            holder.tvMesaEstado.text = "Reservada"
            // Color rojo para indicar que ya está ocupada/pagada
            holder.tvMesaEstado.setTextColor(holder.itemView.context.getColor(android.R.color.holo_red_dark))
            // Deshabilitamos el clic en mesas ya reservadas
            holder.cardMesa.setOnClickListener(null)
        }
    }

    override fun getItemCount() = mesas.size
}
