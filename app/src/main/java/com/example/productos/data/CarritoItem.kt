package com.example.productos.data

data class CarritoItem(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String?,
    val cantidad: Int
)