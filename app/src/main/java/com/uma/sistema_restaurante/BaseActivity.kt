package com.uma.sistema_restaurante

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var toolbar: Toolbar

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_base_drawer, null) as DrawerLayout
        val activityContainer = fullView.findViewById<FrameLayout>(R.id.activity_content)
        
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullView)

        drawerLayout = fullView
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            android.R.string.ok, android.R.string.cancel
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Manejo moderno del botón atrás
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mesas -> {
                if (this.javaClass != MainActivity::class.java) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            R.id.nav_productos -> {
                if (this.javaClass != ProductListActivity::class.java) {
                    startActivity(Intent(this, ProductListActivity::class.java))
                    if (this !is MainActivity) finish()
                }
            }
            R.id.nav_ordenes -> {
                if (this.javaClass != OrderListActivity::class.java) {
                    startActivity(Intent(this, OrderListActivity::class.java))
                    if (this !is MainActivity) finish()
                }
            }
            R.id.nav_reservas -> {
                if (this.javaClass != TableListActivity::class.java) {
                    startActivity(Intent(this, TableListActivity::class.java))
                    if (this !is MainActivity) finish()
                }
            }
            R.id.nav_register -> {
                if (this.javaClass != RegisterActivity::class.java) {
                    startActivity(Intent(this, RegisterActivity::class.java))
                    if (this !is MainActivity) finish()
                }
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
