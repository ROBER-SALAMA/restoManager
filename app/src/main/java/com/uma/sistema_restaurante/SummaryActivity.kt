package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: SummaryAdapter
    private var platosOrden = mutableListOf<PlatoOrden>()
    private var mesaId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        db = FirebaseFirestore.getInstance()
        mesaId = intent.getStringExtra("MESA_ID") ?: ""
        @Suppress("DEPRECATION")
        platosOrden = intent.getSerializableExtra("PLATOS_ORDEN") as? ArrayList<PlatoOrden> ?: mutableListOf()

        val rvResumen = findViewById<RecyclerView>(R.id.rvProductosResumen)
        rvResumen.layoutManager = LinearLayoutManager(this)
        adapter = SummaryAdapter(platosOrden)
        rvResumen.adapter = adapter

        val tvTotal = findViewById<TextView>(R.id.tvTotalResumen)
        val total = platosOrden.sumOf { it.precio * it.cantidad }
        tvTotal.text = String.format(Locale.getDefault(), "Total: $%.2f", total)

        val etNombreCliente = findViewById<EditText>(R.id.etNombreCliente)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarReserva)

        btnConfirmar.setOnClickListener {
            val nombreCliente = etNombreCliente.text.toString()
            if (nombreCliente.isNotEmpty()) {
                guardarReserva(nombreCliente, total)
            } else {
                Toast.makeText(this, "Ingresa el nombre del cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarReserva(nombre: String, total: Double) {
        val updates = hashMapOf(
            "estado" to "reservada",
            "clienteNombre" to nombre,
            "platosReservados" to platosOrden,
            "totalReserva" to total
        )

        db.collection("mesas").document(mesaId)
            .update(updates as Map<String, Any>)
            .addOnSuccessListener {
                val nuevaOrden = Orden(
                    mesaId = mesaId,
                    clienteNombre = nombre,
                    platos = platosOrden,
                    total = total,
                    estado = "pendiente"
                )
                db.collection("ordenes").add(nuevaOrden)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Reserva guardada con éxito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar reserva", Toast.LENGTH_SHORT).show()
            }
    }
}
