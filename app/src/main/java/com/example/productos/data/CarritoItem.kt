package com.example.productos.data

data class CarritoItem(
    val id: Int, // mismo id del producto
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    val cantidad: Int
)
