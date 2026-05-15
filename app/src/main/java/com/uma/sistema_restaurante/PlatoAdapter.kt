package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uma.sistema_restaurante.R

/**
 * Adaptador para la lista de platos en el menú.
 */
class PlatoAdapter(
    private var platos: List<Plato>
) : RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder>() {

    class PlatoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPlato: ImageView = view.findViewById(R.id.ivPlato)
        val tvPlatoNombre: TextView = view.findViewById(R.id.tvPlatoNombre)
        val tvPlatoDescripcion: TextView = view.findViewById(R.id.tvPlatoDescripcion)
        val tvPlatoPrecio: TextView = view.findViewById(R.id.tvPlatoPrecio)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val btnAumentar: ImageButton = view.findViewById(R.id.btnAumentar)
        val btnDisminuir: ImageButton = view.findViewById(R.id.btnDisminuir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plato, parent, false)
        return PlatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val plato = platos[position]
        holder.tvPlatoNombre.text = plato.nombre
        holder.tvPlatoDescripcion.text = plato.descripcion
        holder.tvPlatoPrecio.text = "$${String.format("%.2f", plato.precio)}"
        holder.tvCantidad.text = plato.cantidadSeleccionada.toString()

        if (plato.imagen.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(plato.imagen).into(holder.ivPlato)
        } else {
            holder.ivPlato.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        holder.btnAumentar.setOnClickListener {
            plato.cantidadSeleccionada++
            holder.tvCantidad.text = plato.cantidadSeleccionada.toString()
        }

        holder.btnDisminuir.setOnClickListener {
            if (plato.cantidadSeleccionada > 0) {
                plato.cantidadSeleccionada--
                holder.tvCantidad.text = plato.cantidadSeleccionada.toString()
            }
        }
    }

    override fun getItemCount() = platos.size

    fun updateList(newList: List<Plato>) {
        platos = newList
        notifyDataSetChanged()
    }
}
