package com.uma.sistema_restaurante

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Actividad encargada de procesar el pago de la reserva.
 * Solicita los datos de la tarjeta y actualiza el estado de la mesa en la base de datos.
 */
class PaymentActivity : AppCompatActivity() {

    // Instancia de Firestore
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        // Obtener datos enviados desde FoodMenuActivity
        val mesaId = intent.getStringExtra("MESA_ID") ?: ""
        val total = intent.getDoubleExtra("TOTAL", 0.0)

        // Obtener lista de platos seleccionados
        @Suppress("UNCHECKED_CAST")
        val platosSeleccionados =
            intent.getSerializableExtra("PLATOS") as? ArrayList<Plato> ?: arrayListOf()

        // Referencias UI
        val tvTotal = findViewById<TextView>(R.id.tvTotalPagar)
        val etNombre = findViewById<EditText>(R.id.etNombreTarjeta)
        val etNumero = findViewById<EditText>(R.id.etNumeroTarjeta)
        val etFecha = findViewById<EditText>(R.id.etFechaExpiracion)
        val etCVV = findViewById<EditText>(R.id.etCVV)
        val rbVisa = findViewById<RadioButton>(R.id.rbVisa)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        // Mostrar total
        tvTotal.text = "Total a pagar: $${String.format("%.2f", total)}"

        // Acción botón pagar
        btnPagar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val numero = etNumero.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val cvv = etCVV.text.toString().trim()

            // Validaciones básicas
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese el nombre de la tarjeta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (numero.length < 16) {
                Toast.makeText(this, "Número de tarjeta inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (fecha.isEmpty()) {
                Toast.makeText(this, "Ingrese la fecha de expiración", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cvv.length < 3) {
                Toast.makeText(this, "CVV inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tipo de tarjeta
            val tipoTarjeta = if (rbVisa.isChecked) {
                "Visa"
            } else {
                "Mastercard"
            }

            // Procesar pago y guardar reserva
            guardarReservaEnFirestore(
                mesaId = mesaId,
                platos = platosSeleccionados,
                total = total
            )
        }
    }

    /**
     * Guarda la reserva en Firestore.
     */
    private fun guardarReservaEnFirestore(
        mesaId: String,
        platos: List<Plato>,
        total: Double
    ) {

        // Convertir platos a PlatoOrden
        val platosOrdenados = platos.map {
            PlatoOrden(
                id = it.id,
                nombre = it.nombre,
                precio = it.precio,
                cantidad = it.cantidadSeleccionada
            )
        }

        // Datos a guardar
        val mesaData = mapOf(
            "id" to mesaId,
            "estado" to "reservada",
            "platosReservados" to platosOrdenados,
            "totalReserva" to total
        )

        // Guardar en Firestore
        db.collection("mesas")
            .document(mesaId)
            .set(mesaData)
            .addOnSuccessListener {

                // Actualizar datos localmente
                val mesaLocal = RestauranteData.mesas.find {
                    it.id == mesaId
                }

                mesaLocal?.apply {
                    estado = "reservada"
                    platosReservados = platosOrdenados.toMutableList()
                    totalReserva = total
                }

                Toast.makeText(
                    this,
                    "¡Pago exitoso y reserva confirmada!",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
            .addOnFailureListener { e ->

                Toast.makeText(
                    this,
                    "Error al guardar reserva: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}