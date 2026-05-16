package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrderListActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: OrderAdapter
    private var listaOrdenes = mutableListOf<Orden>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        db = FirebaseFirestore.getInstance()
        
        // El Toolbar ya está configurado en BaseActivity
        supportActionBar?.title = "Órdenes Pendientes"

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
        db.collection("mesas").document(orden.mesaId)
            .update(mapOf(
                "estado" to "libre",
                "clienteNombre" to "",
                "platosReservados" to listOf<PlatoOrden>(),
                "totalReserva" to 0.0
            ))
            .addOnSuccessListener {
                db.collection("ordenes").document(orden.id)
                    .update("estado", "cancelada")
                    .addOnSuccessListener {
                        Toast.makeText(this, "Orden cancelada", Toast.LENGTH_SHORT).show()
                    }
            }
    }
}
