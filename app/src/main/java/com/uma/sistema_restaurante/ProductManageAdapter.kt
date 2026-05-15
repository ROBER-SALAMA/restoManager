package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductManageAdapter(
    private var productos: List<Plato>,
    private val onEdit: (Plato) -> Unit,
    private val onDelete: (Plato) -> Unit
) : RecyclerView.Adapter<ProductManageAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivPlato)
        val tvNombre: TextView = view.findViewById(R.id.tvPlatoNombre)
        val tvPrecio: TextView = view.findViewById(R.id.tvPlatoPrecio)
        val btnEdit: ImageButton = view.findViewById(R.id.btnAumentar) // Reusing IDs for speed, but should ideally be specific
        val btnDelete: ImageButton = view.findViewById(R.id.btnDisminuir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plato, parent, false)
        // Adjusting visibility for management
        view.findViewById<View>(R.id.tvCantidad).visibility = View.GONE
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val plato = productos[position]
        holder.tvNombre.text = plato.nombre
        holder.tvPrecio.text = "$${String.format("%.2f", plato.precio)}"
        
        if (plato.imagen.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(plato.imagen).into(holder.ivProduct)
        }

        // Overriding the behavior of buttons for management
        holder.btnEdit.setImageResource(android.R.drawable.ic_menu_edit)
        holder.btnDelete.setImageResource(android.R.drawable.ic_menu_delete)

        holder.btnEdit.setOnClickListener { onEdit(plato) }
        holder.btnDelete.setOnClickListener { onDelete(plato) }
    }

    override fun getItemCount() = productos.size

    fun updateList(newList: List<Plato>) {
        productos = newList
        notifyDataSetChanged()
    }
}
