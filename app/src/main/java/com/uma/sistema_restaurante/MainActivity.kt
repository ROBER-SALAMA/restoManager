package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Actividad principal que muestra el mapa de mesas del restaurante.
 * Permite visualizar qué mesas están disponibles y cuáles están reservadas.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MesaAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos Firestore para obtener el estado actual de las mesas
        db = FirebaseFirestore.getInstance()

        // Configuración del RecyclerView para mostrar las mesas en un formato de rejilla (2 columnas)
        val rvMesas = findViewById<RecyclerView>(R.id.rvMesas)
        rvMesas.layoutManager = GridLayoutManager(this, 2)

        // Inicializamos el adaptador con la lista de mesas local
        adapter = MesaAdapter(RestauranteData.mesas) { mesa ->
            // Al hacer clic en una mesa disponible, abrimos el menú de comida
            val intent = Intent(this, FoodMenuActivity::class.java)
            intent.putExtra("MESA_ID", mesa.id)
            startActivity(intent)
        }
        rvMesas.adapter = adapter
        
        // Sincronizamos con la base de datos para obtener el estado real de las mesas
        sincronizarMesasDesdeFirestore()
    }

    /**
     * Se ejecuta cada vez que el usuario vuelve a esta pantalla.
     * Sirve para refrescar el estado de las mesas después de un pago exitoso.
     */
    override fun onResume() {
        super.onResume()
        sincronizarMesasDesdeFirestore()
    }

    /**
     * Consulta Firestore para saber qué mesas están ocupadas.
     * La persistencia se logra leyendo los documentos de la colección "mesas".
     */
    private fun sincronizarMesasDesdeFirestore() {
        db.collection("mesas")
            .get()
            .addOnSuccessListener { result ->
                // Recorremos los documentos encontrados en Firestore
                for (document in result) {
                    val mesaId = document.getLong("id")?.toInt() ?: -1
                    val estaDisponible = document.getBoolean("estaDisponible") ?: true
                    
                    // Buscamos la mesa correspondiente en nuestra lista local y actualizamos su estado
                    val mesaLocal = RestauranteData.mesas.find { it.id == mesaId }
                    mesaLocal?.estaDisponible = estaDisponible
                }
                // Notificamos al adaptador que los datos cambiaron para refrescar la interfaz (colores de las mesas)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al sincronizar mesas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
