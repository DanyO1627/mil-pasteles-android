package com.example.productos.model

data class ProductoMODEL
    (
    val id: Int,
    val nombre: String,
    val precio: Int,
    val imagen: Int,
    val descripcion: String,
    val descripcionLarga: String,
    val categoriaId: Int,
    val stock: Int
    )
