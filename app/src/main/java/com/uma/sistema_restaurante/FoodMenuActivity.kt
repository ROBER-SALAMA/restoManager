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
import com.uma.sistema_restaurante.R

/**
 * Actividad que muestra el menú de platillos disponibles para ser ordenados.
 * Permite al usuario seleccionar múltiples platos para una mesa específica.
 */
class FoodMenuActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: PlatoAdapter
    private var listaPlatos = mutableListOf<Plato>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_menu)

        // Inicialización de la base de datos Firestore
        db = FirebaseFirestore.getInstance()

        // Obtención del ID de la mesa seleccionada desde el Intent
        val mesaId = intent.getIntExtra("MESA_ID", -1)
        val mesa = RestauranteData.mesas.find { it.id == mesaId }

        // Si la mesa no existe, cerramos la actividad por seguridad
        if (mesa == null) {
            finish()
            return
        }

        // Configuración de la cabecera con el número de mesa
        val tvMesaSeleccionada = findViewById<TextView>(R.id.tvMesaSeleccionada)
        tvMesaSeleccionada.text = "Menú para Mesa $mesaId"

        // Configuración del RecyclerView para listar los platos
        val rvPlatos = findViewById<RecyclerView>(R.id.rvPlatos)
        rvPlatos.layoutManager = LinearLayoutManager(this)
        
        adapter = PlatoAdapter(listaPlatos)
        rvPlatos.adapter = adapter

        // Cargamos los platillos almacenados en la base de datos de Firebase
        cargarMenuDesdeFirestore()

        // Botón para proceder al pago con los platos seleccionados
        val btnGuardarReserva = findViewById<Button>(R.id.btnGuardarReserva)
        btnGuardarReserva.setOnClickListener {
            // Filtramos la lista para obtener solo los platos marcados por el usuario
            val platosSeleccionados = listaPlatos.filter { it.estaSeleccionado }
            
            if (platosSeleccionados.isNotEmpty()) {
                // Calculamos el costo total sumando el precio de cada plato
                val total = platosSeleccionados.sumOf { it.precio }
                
                // Navegamos a la pantalla de pago enviando la información necesaria
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("MESA_ID", mesaId)
                intent.putExtra("TOTAL", total)
                intent.putExtra("PLATOS", ArrayList(platosSeleccionados))
                startActivity(intent)
                finish() // Cerramos el menú una vez enviada la orden
            } else {
                Toast.makeText(this, "Selecciona al menos un plato", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Recupera la lista de platillos desde la colección "menu" en Firestore.
     */
    private fun cargarMenuDesdeFirestore() {
        db.collection("menu")
            .get()
            .addOnSuccessListener { result ->
                listaPlatos.clear()
                for (document in result) {
                    // Convertimos el documento de Firestore a un objeto Plato
                    val plato = document.toObject(Plato::class.java)
                    listaPlatos.add(plato)
                }
                // Actualizamos la lista visual
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar platos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
