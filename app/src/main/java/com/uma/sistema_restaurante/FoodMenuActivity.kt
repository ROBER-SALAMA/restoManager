package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: PlatoAdapter
    private var listaPlatos = mutableListOf<Plato>()
    private var mesaId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_menu)

        db = FirebaseFirestore.getInstance()
        mesaId = intent.getStringExtra("MESA_ID") ?: ""

        val tvMesaSeleccionada = findViewById<TextView>(R.id.tvMesaSeleccionada)
        tvMesaSeleccionada.text = "Pedido para Mesa $mesaId"

        val rvPlatos = findViewById<RecyclerView>(R.id.rvPlatos)
        rvPlatos.layoutManager = LinearLayoutManager(this)
        
        adapter = PlatoAdapter(listaPlatos)
        rvPlatos.adapter = adapter

        val svBuscarPlato = findViewById<SearchView>(R.id.svBuscarPlato)
        svBuscarPlato.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarMenu(newText)
                return true
            }
        })

        cargarMenuDesdeFirestore()

        findViewById<Button>(R.id.btnGuardarReserva).setOnClickListener {
            val seleccionados = listaPlatos.filter { it.cantidadSeleccionada > 0 }
            if (seleccionados.isNotEmpty()) {
                val intent = Intent(this, SummaryActivity::class.java)
                intent.putExtra("MESA_ID", mesaId)
                val platosOrden = seleccionados.map { 
                    PlatoOrden(it.id, it.nombre, it.precio, it.cantidadSeleccionada) 
                }
                intent.putExtra("PLATOS_ORDEN", ArrayList(platosOrden))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Selecciona al menos un producto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarMenuDesdeFirestore() {
        db.collection("menu").get().addOnSuccessListener { result ->
            listaPlatos.clear()
            for (document in result) {
                val plato = document.toObject(Plato::class.java)
                plato.id = document.id
                listaPlatos.add(plato)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun filtrarMenu(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            listaPlatos
        } else {
            listaPlatos.filter { it.nombre.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) }
        }
        adapter.updateList(filteredList)
    }
}
