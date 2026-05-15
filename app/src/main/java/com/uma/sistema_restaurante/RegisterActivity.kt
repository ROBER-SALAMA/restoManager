package com.uma.sistema_restaurante

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uma.sistema_restaurante.R

/**
 * Actividad para el registro de nuevos usuarios (Staff del restaurante).
 * Crea una cuenta en Firebase Auth y guarda los detalles adicionales en Firestore.
 */
class RegisterActivity : AppCompatActivity() {

    // Servicios de Firebase para autenticación y base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar los servicios
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Referencias a las vistas del formulario de registro
        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etRegisterPassword)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        // Acción al presionar el botón de Registrar
        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validación de que ningún campo esté vacío
            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Crear usuario en Firebase Auth
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Si se crea con éxito, obtenemos el ID único generado
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                // Guardamos los datos adicionales (nombre, rol) en Firestore
                                saveUserToFirestore(userId, fullName, email)
                            }
                        } else {
                            // Manejo de errores (ej. email ya registrado, contraseña muy corta)
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para volver a la pantalla de login
        tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    /**
     * Guarda la información extendida del usuario en la colección "users" de Firestore.
     */
    private fun saveUserToFirestore(uid: String, name: String, email: String) {
        // Mapa de datos del usuario
        val userMap = hashMapOf(
            "uid" to uid,
            "fullName" to name,
            "email" to email,
            "role" to "staff", // Rol por defecto
            "createdAt" to com.google.firebase.Timestamp.now()
        )

        // Escritura en el documento específico del usuario
        db.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                finish() // Regresa al Login tras el éxito
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar perfil: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
