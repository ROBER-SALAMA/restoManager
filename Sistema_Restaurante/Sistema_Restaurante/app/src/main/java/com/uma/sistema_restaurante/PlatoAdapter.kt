package com.uma.sistema_restaurante

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uma.sistema_restaurante.R

/**
 * Adaptador para la lista de platos en el menú.
 * Gestiona la visualización de cada platillo y captura la selección del usuario.
 */
class PlatoAdapter(
    private val platos: List<Plato>
) : RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder>() {

    /**
     * ViewHolder que contiene las referencias a las vistas de cada elemento de la lista.
     */
    class PlatoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbPlato: CheckBox = view.findViewById(R.id.cbPlato)
        val tvPlatoNombre: TextView = view.findViewById(R.id.tvPlatoNombre)
        val tvPlatoDescripcion: TextView = view.findViewById(R.id.tvPlatoDescripcion)
        val tvPlatoPrecio: TextView = view.findViewById(R.id.tvPlatoPrecio)
    }

    /**
     * Infla el diseño XML (item_plato) para cada fila del RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plato, parent, false)
        return PlatoViewHolder(view)
    }

    /**
     * Vincula los datos de un objeto Plato con las vistas del ViewHolder.
     */
    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val plato = platos[position]
        holder.tvPlatoNombre.text = plato.nombre
        holder.tvPlatoDescripcion.text = plato.descripcion
        // Formateo de precio a dos decimales
        holder.tvPlatoPrecio.text = "$${String.format("%.2f", plato.precio)}"
        
        // Limpiamos el listener antes de asignar el estado para evitar ejecuciones accidentales al reciclar vistas
        holder.cbPlato.setOnCheckedChangeListener(null)
        holder.cbPlato.isChecked = plato.estaSeleccionado
        
        // Listener para actualizar el modelo de datos cuando el usuario marca/desmarca un plato
        holder.cbPlato.setOnCheckedChangeListener { _, isChecked ->
            plato.estaSeleccionado = isChecked
        }
    }

    /**
     * Devuelve la cantidad total de platos en la lista.
     */
    override fun getItemCount() = platos.size
}
