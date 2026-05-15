package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ProductListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ProductManageAdapter
    private var listaProductos = mutableListOf<Plato>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        db = FirebaseFirestore.getInstance()

        val rv = findViewById<RecyclerView>(R.id.rvProductsManage)
        rv.layoutManager = LinearLayoutManager(this)
        
        adapter = ProductManageAdapter(listaProductos, 
            onEdit = { plato ->
                val intent = Intent(this, AddEditProductActivity::class.java)
                intent.putExtra("PRODUCTO", plato)
                startActivity(intent)
            },
            onDelete = { plato ->
                eliminarProducto(plato)
            }
        )
        rv.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            startActivity(Intent(this, AddEditProductActivity::class.java))
        }

        cargarProductos()
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        db.collection("menu").get().addOnSuccessListener { result ->
            listaProductos.clear()
            for (document in result) {
                val plato = document.toObject(Plato::class.java)
                plato.id = document.id
                listaProductos.add(plato)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun eliminarProducto(plato: Plato) {
        db.collection("menu").document(plato.id).delete().addOnSuccessListener {
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
            cargarProductos()
        }
    }
}
