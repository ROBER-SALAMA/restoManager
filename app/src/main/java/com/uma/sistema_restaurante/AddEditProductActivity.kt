package com.uma.sistema_restaurante

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var producto: Plato? = null
    private var imageUri: Uri? = null
    
    private lateinit var ivPreview: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etDesc: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etImagenUrl: EditText
    private lateinit var etTotal: EditText

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            ivPreview.setImageURI(it)
        }
    }

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

        ivPreview = findViewById(R.id.ivProductPreview)
        etNombre = findViewById(R.id.etProductName)
        etDesc = findViewById(R.id.etProductDesc)
        etPrecio = findViewById(R.id.etProductPrice)
        etImagenUrl = findViewById(R.id.etProductImage)
        etTotal = findViewById(R.id.etProductTotal)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        val btnGuardar = findViewById<Button>(R.id.btnSaveProduct)

        btnSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        if (producto != null) {
            supportActionBar?.title = "Editar Producto"
            producto?.let {
                etNombre.setText(it.nombre)
                etDesc.setText(it.descripcion)
                etPrecio.setText(it.precio.toString())
                etImagenUrl.setText(it.imagen)
                etTotal.setText(it.total.toString())
                
                if (it.imagen.isNotEmpty()) {
                    Glide.with(this).load(it.imagen).into(ivPreview)
                }
            }
        } else {
            supportActionBar?.title = "Agregar Producto"
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                convertirImagenABase64YGuardar(nombre)
            } else {
                guardarProducto(nombre, etImagenUrl.text.toString())
            }
        }
    }

    private fun convertirImagenABase64YGuardar(nombre: String) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri!!)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            
            // Redimensionar la imagen si es muy grande para ahorrar espacio en Firestore
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 512, 512, true)
            
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            
            val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            val dataUrl = "data:image/jpeg;base64,$base64String"
            
            guardarProducto(nombre, dataUrl)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al procesar la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarProducto(nombre: String, urlImagen: String) {
        val desc = etDesc.text.toString()
        val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
        val total = etTotal.text.toString().toIntOrNull() ?: 0

        val data = hashMapOf(
            "nombre" to nombre,
            "descripcion" to desc,
            "precio" to precio,
            "imagen" to urlImagen,
            "total" to total
        )

        if (producto == null) {
            db.collection("menu").add(data).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al crear: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            db.collection("menu").document(producto!!.id).update(data as Map<String, Any>)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
