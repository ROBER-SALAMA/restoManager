package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrderListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: OrderAdapter
    private var listaOrdenes = mutableListOf<Orden>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        db = FirebaseFirestore.getInstance()

        val rv = findViewById<RecyclerView>(R.id.rvOrders)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = OrderAdapter(listaOrdenes,
            onCancel = { orden -> cancelarOrden(orden) },
            onBill = { orden ->
                val intent = Intent(this, BillingActivity::class.java)
                intent.putExtra("ORDEN", orden)
                startActivity(intent)
            }
        )
        rv.adapter = adapter

        cargarOrdenes()
    }

    private fun cargarOrdenes() {
        db.collection("ordenes")
            .whereEqualTo("estado", "pendiente")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                listaOrdenes.clear()
                for (doc in value!!) {
                    val orden = doc.toObject(Orden::class.java)
                    orden.id = doc.id
                    listaOrdenes.add(orden)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun cancelarOrden(orden: Orden) {
        // 1. Liberar la mesa
        db.collection("mesas").document(orden.mesaId)
            .update(mapOf(
                "estado" to "libre",
                "clienteNombre" to "",
                "platosReservados" to listOf<PlatoOrden>(),
                "totalReserva" to 0.0
            ))
            .addOnSuccessListener {
                // 2. Actualizar estado de la orden
                db.collection("ordenes").document(orden.id)
                    .update("estado", "cancelada")
                    .addOnSuccessListener {
                        Toast.makeText(this, "Orden cancelada y mesa liberada", Toast.LENGTH_SHORT).show()
                    }
            }
    }
}
