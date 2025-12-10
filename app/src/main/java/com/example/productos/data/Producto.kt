package com.example.productos.data

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    val descripcion: String,
    val descripcionLarga: String,
    val categoriaId: Int,
    var stock: Int
)