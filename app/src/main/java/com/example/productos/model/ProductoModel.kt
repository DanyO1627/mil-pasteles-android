package com.example.productos.model

data class ProductoModel
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
