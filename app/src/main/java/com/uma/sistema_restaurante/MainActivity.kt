package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : BaseActivity() {

    private lateinit var adapter: MesaAdapter
    private lateinit var db: FirebaseFirestore
    private var listaMesas = mutableListOf<Mesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()
        
        // El toolbar ya está configurado por BaseActivity
        supportActionBar?.title = "Nueva Orden"

        val rvMesas = findViewById<RecyclerView>(R.id.rvMesas)
        rvMesas.layoutManager = GridLayoutManager(this, 2)

        adapter = MesaAdapter(listaMesas) { mesa ->
            val intent = Intent(this, FoodMenuActivity::class.java)
            intent.putExtra("MESA_ID", mesa.id)
            startActivity(intent)
        }
        rvMesas.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddMesaMain).setOnClickListener {
            startActivity(Intent(this, AddEditTableActivity::class.java))
        }

        sincronizarMesasDesdeFirestore()
    }

    override fun onResume() {
        super.onResume()
        sincronizarMesasDesdeFirestore()
    }

    private fun sincronizarMesasDesdeFirestore() {
        db.collection("mesas")
            .orderBy("id")
            .get()
            .addOnSuccessListener { result ->
                listaMesas.clear()
                for (document in result) {
                    val mesa = document.toObject(Mesa::class.java)
                    listaMesas.add(mesa)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
