package com.uma.sistema_restaurante

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var producto: Plato? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        db = FirebaseFirestore.getInstance()
        @Suppress("DEPRECATION")
        producto = intent.getSerializableExtra("PRODUCTO") as? Plato

        val etNombre = findViewById<EditText>(R.id.etProductName)
        val etDesc = findViewById<EditText>(R.id.etProductDesc)
        val etPrecio = findViewById<EditText>(R.id.etProductPrice)
        val etImagen = findViewById<EditText>(R.id.etProductImage)
        val etTotal = findViewById<EditText>(R.id.etProductTotal)
        val btnGuardar = findViewById<Button>(R.id.btnSaveProduct)

        if (producto != null) {
            supportActionBar?.title = "Editar Producto"
            producto?.let {
                etNombre.setText(it.nombre)
                etDesc.setText(it.descripcion)
                etPrecio.setText(it.precio.toString())
                etImagen.setText(it.imagen)
                etTotal.setText(it.total.toString())
            }
        } else {
            supportActionBar?.title = "Agregar Producto"
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val desc = etDesc.text.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val imagen = etImagen.text.toString()
            val total = etTotal.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotEmpty()) {
                val data = hashMapOf(
                    "nombre" to nombre,
                    "descripcion" to desc,
                    "precio" to precio,
                    "imagen" to imagen,
                    "total" to total
                )

                if (producto == null) {
                    db.collection("menu").add(data).addOnSuccessListener {
                        finish()
                    }
                } else {
                    db.collection("menu").document(producto!!.id).update(data as Map<String, Any>).addOnSuccessListener {
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
