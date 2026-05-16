package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class TableListActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: TableManageAdapter
    private var listaMesas = mutableListOf<Mesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_list)

        db = FirebaseFirestore.getInstance()
        
        // Título en el Toolbar de BaseActivity
        supportActionBar?.title = "Administrar Mesas"

        val rv = findViewById<RecyclerView>(R.id.rvTablesManage)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = TableManageAdapter(listaMesas, 
            onEdit = { mesa ->
                val intent = Intent(this, AddEditTableActivity::class.java)
                intent.putExtra("MESA", mesa)
                startActivity(intent)
            },
            onDelete = { mesa ->
                mostrarDialogoConfirmacion(mesa)
            }
        )
        rv.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddTable).setOnClickListener {
            startActivity(Intent(this, AddEditTableActivity::class.java))
        }

        cargarMesas()
    }

    override fun onResume() {
        super.onResume()
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
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarDialogoConfirmacion(mesa: Mesa) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Mesa")
            .setMessage("¿Estás seguro de que deseas eliminar la mesa '${mesa.id}'?")
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
                Toast.makeText(this, "Mesa eliminada", Toast.LENGTH_SHORT).show()
                cargarMesas()
            }
    }
}
