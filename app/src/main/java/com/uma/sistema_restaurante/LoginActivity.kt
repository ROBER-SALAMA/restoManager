package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.uma.sistema_restaurante.R

/**
 * Pantalla de Inicio de Sesión.
 * Utiliza Firebase Authentication para validar las credenciales del usuario (Staff).
 */
class LoginActivity : AppCompatActivity() {

    // Instancia para gestionar la autenticación con Firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencias a los componentes de la interfaz de usuario
        val etEmail = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegister = findViewById<TextView>(R.id.btnRegister)

        // Acción del botón Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Proceso de autenticación con el servicio de Firebase
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Si el login es correcto, navegamos a la pantalla de mesas (MainActivity)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Finalizar esta actividad para que el usuario no regrese con "atrás"
                        } else {
                            // Mostrar mensaje detallado del error de autenticación
                            Toast.makeText(this, "Error de acceso: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Navegar a la pantalla de Registro de nuevo usuario
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
