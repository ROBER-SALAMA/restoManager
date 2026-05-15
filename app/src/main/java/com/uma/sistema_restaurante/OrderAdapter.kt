package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class OrderAdapter(
    private var ordenes: List<Orden>,
    private val onCancel: (Orden) -> Unit,
    private val onBill: (Orden) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMesa: TextView = view.findViewById(R.id.tvOrderMesa)
        val tvCliente: TextView = view.findViewById(R.id.tvOrderCliente)
        val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
        val btnCancel: Button = view.findViewById(R.id.btnCancelOrder)
        val btnBill: Button = view.findViewById(R.id.btnBillOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orden = ordenes[position]
        holder.tvMesa.text = "Mesa: ${orden.mesaId}"
        holder.tvCliente.text = "Cliente: ${orden.clienteNombre}"
        holder.tvTotal.text = String.format(Locale.getDefault(), "Total: $%.2f", orden.total)

        holder.btnCancel.setOnClickListener { onCancel(orden) }
        holder.btnBill.setOnClickListener { onBill(orden) }
    }

    override fun getItemCount() = ordenes.size

    fun updateList(newList: List<Orden>) {
        ordenes = newList
        notifyDataSetChanged()
    }
}
