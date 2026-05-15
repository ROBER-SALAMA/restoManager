package com.uma.sistema_restaurante

import java.io.Serializable

/**
 * Clase que representa una Mesa en el restaurante.
 */
data class Mesa(
    var id: Int = 0,
    var capacidad: Int = 0,
    var estado: String = "libre",
    var platosReservados: MutableList<PlatoOrden> = mutableListOf(),
    var totalReserva: Double = 0.0,
    var clienteNombre: String = ""
) : Serializable

/**
 * Clase que representa un Plato del menú en la colección "menu".
 */
data class Plato(
    var id: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var imagen: String = "",
    var precio: Double = 0.0,
    var total: Int = 0,
    var cantidadSeleccionada: Int = 0 // Temporal para la UI de selección
) : Serializable

/**
 * Clase que representa un plato dentro de una orden o reserva.
 */
data class PlatoOrden(
    var id: String = "",
    var nombre: String = "",
    var precio: Double = 0.0,
    var cantidad: Int = 0
) : Serializable

/**
 * Clase que representa una Orden general.
 */
data class Orden(
    var id: String = "",
    var mesaId: String = "",
    var clienteNombre: String = "",
    var platos: List<PlatoOrden> = listOf(),
    var total: Double = 0.0,
    var estado: String = "pendiente" // pendiente, facturada, cancelada
) : Serializable

data class Usuario(
    val nombreUsuario: String = "",
    val contrasena: String = ""
)

object RestauranteData {
    val usuarios = mutableListOf<Usuario>()
    var menuFirestore = mutableListOf<Plato>()
    var mesas = mutableListOf<Mesa>()
}
