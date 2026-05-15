package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        val rv = findViewById<RecyclerView>(R.id.rvTablesManage)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = TableManageAdapter(listaMesas, 
            onEdit = { mesa ->
                val intent = Intent(this, AddEditTableActivity::class.java)
                intent.putExtra("MESA", mesa)
                startActivity(intent)
            },
            onDelete = { mesa ->
                eliminarMesa(mesa)
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
        db.collection("mesas").get().addOnSuccessListener { result ->
            listaMesas.clear()
            for (document in result) {
                val mesa = document.toObject(Mesa::class.java)
                mesa.id = document.id.toInt()
                listaMesas.add(mesa)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun eliminarMesa(mesa: Mesa) {
        db.collection("mesas").document(mesa.id.toString()).delete().addOnSuccessListener {
            Toast.makeText(this, "Mesa eliminada", Toast.LENGTH_SHORT).show()
            cargarMesas()
        }
    }
}
