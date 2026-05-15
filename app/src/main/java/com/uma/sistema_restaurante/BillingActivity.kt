package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class BillingActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: SummaryAdapter
    private var orden: Orden? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        db = FirebaseFirestore.getInstance()
        @Suppress("DEPRECATION")
        orden = intent.getSerializableExtra("ORDEN") as? Orden

        if (orden == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvBillingMesa).text = "Mesa: ${orden?.mesaId}"
        findViewById<TextView>(R.id.tvBillingCliente).text = "Cliente: ${orden?.clienteNombre}"

        val rv = findViewById<RecyclerView>(R.id.rvBillingItems)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = SummaryAdapter(orden?.platos ?: listOf())
        rv.adapter = adapter

        val tvTotal = findViewById<TextView>(R.id.tvBillingTotal)
        tvTotal.text = String.format(Locale.getDefault(), "Total a Pagar: $%.2f", orden?.total ?: 0.0)

        findViewById<Button>(R.id.btnCompleteBilling).setOnClickListener {
            finalizarFacturacion()
        }
    }

    private fun finalizarFacturacion() {
        val o = orden ?: return
        // 1. Liberar la mesa
        db.collection("mesas").document(o.mesaId)
            .update(mapOf(
                "estado" to "libre",
                "clienteNombre" to "",
                "platosReservados" to listOf<PlatoOrden>(),
                "totalReserva" to 0.0
            ))
            .addOnSuccessListener {
                // 2. Marcar orden como facturada
                db.collection("ordenes").document(o.id)
                    .update("estado", "facturada")
                    .addOnSuccessListener {
                        Toast.makeText(this, "Facturación completada", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
            }
    }
}
