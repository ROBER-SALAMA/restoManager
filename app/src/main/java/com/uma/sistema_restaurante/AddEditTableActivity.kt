package com.uma.sistema_restaurante

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore

class AddEditTableActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var mesa: Mesa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_table)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        db = FirebaseFirestore.getInstance()
        @Suppress("DEPRECATION")
        mesa = intent.getSerializableExtra("MESA") as? Mesa

        val etTableId = findViewById<EditText>(R.id.etTableId)
        val etCapacity = findViewById<EditText>(R.id.etTableCapacity)
        val spStatus = findViewById<Spinner>(R.id.spTableStatus)
        val btnSave = findViewById<Button>(R.id.btnSaveTable)
        val btnDelete = findViewById<Button>(R.id.btnDeleteTable)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.table_status_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = adapter

        // Si estamos editando
        if (mesa != null) {
            supportActionBar?.title = "Editar Mesa"
            mesa?.let {
                etTableId.setText(it.id)
                etTableId.isEnabled = false 
                etCapacity.setText(it.capacidad.toString())
                
                val statusArray = resources.getStringArray(R.array.table_status_array)
                val index = statusArray.indexOf(it.estado)
                if (index >= 0) spStatus.setSelection(index)
                
                btnSave.text = "Actualizar Mesa"
                btnDelete.visibility = View.VISIBLE
            }
        } else {
            supportActionBar?.title = "Agregar Mesa"
        }

        btnSave.setOnClickListener {
            val id = etTableId.text.toString().trim()
            val capacity = etCapacity.text.toString().toIntOrNull() ?: 0
            val status = spStatus.selectedItem.toString()

            if (id.isNotEmpty()) {
                val data = hashMapOf(
                    "id" to id,
                    "capacidad" to capacity,
                    "estado" to status
                )

                db.collection("mesas").document(id)
                    .set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Mesa guardada correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "El nombre de la mesa es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            mostrarDialogoEliminar()
        }
    }

    private fun mostrarDialogoEliminar() {
        mesa?.let { m ->
            AlertDialog.Builder(this)
                .setTitle("Eliminar Mesa")
                .setMessage("¿Estás seguro de que deseas eliminar la mesa '${m.id}'?")
                .setPositiveButton("Eliminar") { _, _ ->
                    db.collection("mesas").document(m.id.toString()).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Mesa eliminada", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
