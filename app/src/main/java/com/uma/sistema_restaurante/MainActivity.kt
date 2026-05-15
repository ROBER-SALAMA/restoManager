package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adapter: MesaAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var drawerLayout: DrawerLayout
    private var listaMesas = mutableListOf<Mesa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            android.R.string.ok,
            android.R.string.cancel
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val rvMesas = findViewById<RecyclerView>(R.id.rvMesas)

        rvMesas.layoutManager = GridLayoutManager(this, 2)

        adapter = MesaAdapter(listaMesas) { mesa ->
            val intent = Intent(this, FoodMenuActivity::class.java)

            intent.putExtra("MESA_ID", mesa.id)

            startActivity(intent)
        }

        rvMesas.adapter = adapter

        sincronizarMesasDesdeFirestore()

        // Manejo moderno del botón atrás
        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sincronizarMesasDesdeFirestore()
    }

    private fun sincronizarMesasDesdeFirestore() {

        db.collection("mesas")
            .get()
            .addOnSuccessListener { result ->

                listaMesas.clear()

                for (document in result) {

                    val mesa = document.toObject(Mesa::class.java)

                    // Convertimos el ID del documento a Int
                    mesa.id = document.id.toInt()

                    listaMesas.add(mesa)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_mesas -> {
                // Ya estamos aquí
            }

            R.id.nav_productos -> {
                startActivity(Intent(this, ProductListActivity::class.java))
            }

            R.id.nav_ordenes -> {
                startActivity(Intent(this, OrderListActivity::class.java))
            }

            R.id.nav_reservas -> {
                Toast.makeText(
                    this,
                    "Funcionalidad de Reservas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}