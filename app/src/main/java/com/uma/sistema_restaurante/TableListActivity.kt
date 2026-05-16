package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class TableListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TableManageAdapter
    private var listaMesas = mutableListOf<Mesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_list)

        db = FirebaseFirestore.getInstance()

        // Configurar el Toolbar
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarTables)?.setNavigationOnClickListener {
            finish()
        }

        // Configurar el RecyclerView
        val rv = findViewById<RecyclerView>(R.id.rvTablesManage)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = TableManageAdapter(listaMesas, 
            onEdit = { mesa ->
                // Acción para editar: abrir AddEditTableActivity con los datos de la mesa
                val intent = Intent(this, AddEditTableActivity::class.java)
                intent.putExtra("MESA", mesa)
                startActivity(intent)
            },
            onDelete = { mesa ->
                // Acción para eliminar: mostrar confirmación
                mostrarDialogoConfirmacion(mesa)
            }
        )
        rv.adapter = adapter

        // Botón flotante para agregar una nueva mesa
        findViewById<FloatingActionButton>(R.id.fabAddTable).setOnClickListener {
            val intent = Intent(this, AddEditTableActivity::class.java)
            startActivity(intent)
        }

        cargarMesas()
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista cada vez que la actividad vuelve al primer plano
        cargarMesas()
    }

    private fun cargarMesas() {
        db.collection("mesas").orderBy("id").get()
            .addOnSuccessListener { result ->
                listaMesas.clear()
                for (document in result) {
                    val mesa = document.toObject(Mesa::class.java)
                    listaMesas.add(mesa)
                }
                adapter.updateList(listaMesas)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al obtener mesas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoConfirmacion(mesa: Mesa) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Mesa")
            .setMessage("¿Estás seguro de que deseas eliminar la Mesa ${mesa.id}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarMesa(mesa)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarMesa(mesa: Mesa) {
        db.collection("mesas").document(mesa.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Mesa eliminada con éxito", Toast.LENGTH_SHORT).show()
                cargarMesas() // Refrescar la lista
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
