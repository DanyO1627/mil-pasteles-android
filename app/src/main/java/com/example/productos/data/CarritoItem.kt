package com.example.productos.data

data class CarritoItem(
    val productoId: Long,
    val nombre: String,
    val precio: Double,
    val imagen: String,
    val cantidad: Int,
    val imagenUrl: String? = null
)
