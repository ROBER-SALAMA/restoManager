package com.uma.sistema_restaurante

import java.io.Serializable

/**
 * Clase que representa una Mesa en el restaurante.
 * @property id Identificador único de la mesa.
 * @property capacidad Número de personas que pueden sentarse.
 * @property estaDisponible Estado de la mesa (si está libre o ya fue pagada/reservada).
 * @property platosReservados Lista de platos que el cliente ha pedido para esta mesa.
 * @property totalReserva Monto total acumulado de la orden.
 */
data class Mesa(
    val id: Int = 0,
    val capacidad: Int = 0,
    var estaDisponible: Boolean = true,
    var platosReservados: MutableList<Plato> = mutableListOf(),
    var totalReserva: Double = 0.0
) : Serializable

/**
 * Clase que representa un Plato del menú.
 * @property nombre Nombre del platillo.
 * @property descripcion Ingredientes o detalles del plato.
 * @property precio Costo del plato.
 * @property estaSeleccionado Estado booleano para el manejo en el RecyclerView (selección del usuario).
 */
data class Plato(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    @get:JvmName("isEstaSeleccionado")
    var estaSeleccionado: Boolean = false
) : Serializable

/**
 * Clase para el manejo de credenciales de usuario.
 */
data class Usuario(
    val nombreUsuario: String = "",
    val contrasena: String = ""
)

/**
 * Objeto Singleton que contiene datos volátiles o de configuración inicial para la app.
 */
object RestauranteData {
    val usuarios = mutableListOf<Usuario>()
    
    // Lista inicial de mesas por defecto. 
    // Nota: El estado de disponibilidad se sincroniza con Firebase Firestore.
    val mesas = mutableListOf(
        Mesa(1, 4),
        Mesa(2, 6),
        Mesa(3, 2),
        Mesa(4, 5),
        Mesa(5, 4),
        Mesa(6, 8)
    )
    
    // Lista para almacenar el menú cargado desde la nube.
    var menuFirestore = mutableListOf<Plato>()
}
