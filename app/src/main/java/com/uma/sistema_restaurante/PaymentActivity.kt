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

    // Instancia de Firestore para interactuar con la base de datos en la nube
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Inicializamos Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Obtenemos los datos pasados desde FoodMenuActivity
        val mesaId = intent.getIntExtra("MESA_ID", -1)
        val total = intent.getDoubleExtra("TOTAL", 0.0)
        
        // Obtenemos la lista de platos seleccionados para guardarlos en el registro de la reserva
        @Suppress("UNCHECKED_CAST")
        val platosSeleccionados = intent.getSerializableExtra("PLATOS") as? ArrayList<Plato> ?: arrayListOf()

        // Referencias a los componentes de la interfaz
        val tvTotal = findViewById<TextView>(R.id.tvTotalPagar)
        tvTotal.text = "Total a pagar: $${String.format("%.2f", total)}"

        val etNombre = findViewById<EditText>(R.id.etNombreTarjeta)
        val etNumero = findViewById<EditText>(R.id.etNumeroTarjeta)
        val etFecha = findViewById<EditText>(R.id.etFechaExpiracion)
        val etCVV = findViewById<EditText>(R.id.etCVV)
        val rbVisa = findViewById<RadioButton>(R.id.rbVisa)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        // Acción al hacer clic en el botón de pagar
        btnPagar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val numero = etNumero.text.toString()
            val fecha = etFecha.text.toString()
            val cvv = etCVV.text.toString()

            // Validación básica de los campos de la tarjeta
            if (nombre.isEmpty() || numero.length < 16 || fecha.isEmpty() || cvv.length < 3) {
                Toast.makeText(this, "Por favor complete los datos de la tarjeta correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Determinamos el tipo de tarjeta seleccionado
            val tipo = if (rbVisa.isChecked) "Visa" else "Mastercard"
            
            // Si la validación pasa, procedemos a guardar la reserva en Firestore para que sea persistente
            guardarReservaEnFirestore(mesaId, platosSeleccionados, total)
        }
    }

    /**
     * Sube la información de la reserva a Firebase Firestore.
     * Esto asegura que el estado de la mesa se mantenga aunque se cierre la app.
     */
    private fun guardarReservaEnFirestore(mesaId: Int, platos: List<Plato>, total: Double) {
        // Estructura de datos que se enviará a Firestore
        val mesaData = mapOf(
            "id" to mesaId,
            "estaDisponible" to false,
            "platosReservados" to platos,
            "totalReserva" to total
        )

        // Guardamos en la colección "mesas" usando el ID de la mesa como nombre del documento
        db.collection("mesas").document(mesaId.toString())
            .set(mesaData)
            .addOnSuccessListener {
                // Actualizamos el estado localmente para reflejar el cambio de inmediato en la UI
                val mesaLocal = RestauranteData.mesas.find { it.id == mesaId }
                mesaLocal?.apply {
                    estado = "reservada"
                    platosReservados = platos.map {
                        PlatoOrden(
                            id = it.id,
                            nombre = it.nombre,
                            precio = it.precio,
                            cantidad = it.cantidadSeleccionada
                        )
                    }.toMutableList()

                    totalReserva = total
                }
                
                Toast.makeText(this, "¡Pago exitoso y reserva confirmada!", Toast.LENGTH_LONG).show()
                finish() // Regresa a la pantalla principal
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar reserva: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
