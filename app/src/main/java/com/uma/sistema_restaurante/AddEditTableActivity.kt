package com.uma.sistema_restaurante

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddEditTableActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var mesa: Mesa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_table)

        db = FirebaseFirestore.getInstance()
        @Suppress("DEPRECATION")
        mesa = intent.getSerializableExtra("MESA") as? Mesa

        val etTableId = findViewById<EditText>(R.id.etTableId)
        val etCapacity = findViewById<EditText>(R.id.etTableCapacity)
        val spStatus = findViewById<Spinner>(R.id.spTableStatus)
        val btnSave = findViewById<Button>(R.id.btnSaveTable)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.table_status_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = adapter

        mesa?.let {
            etTableId.setText(it.id)
            etTableId.isEnabled = false
            etCapacity.setText(it.capacidad.toString())
            val statusArray = resources.getStringArray(R.array.table_status_array)
            val index = statusArray.indexOf(it.estado)
            if (index >= 0) spStatus.setSelection(index)
        }

        btnSave.setOnClickListener {
            val id = etTableId.text.toString()
            val capacity = etCapacity.text.toString().toIntOrNull() ?: 0
            val status = spStatus.selectedItem.toString()

            if (id.isNotEmpty()) {
                val data = hashMapOf(
                    "id" to id,
                    "capacidad" to capacity,
                    "estado" to status
                )

                if (mesa == null) {
                    db.collection("mesas").document(id).set(data).addOnSuccessListener {
                        finish()
                    }
                } else {
                    db.collection("mesas").document(mesa!!.id.toString()).update(data as Map<String, Any>).addOnSuccessListener {
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "El ID de la mesa es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
