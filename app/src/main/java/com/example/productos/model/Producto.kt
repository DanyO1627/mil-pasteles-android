package com.example.productos.model

data class Producto(
    val id: Long? = null,
    val nombreProducto: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = "",
    val descripcionProducto: String = "",
    val descripcionLarga: String = "",
    val activo: Boolean? = true,
    val stock: Int = 0,
    val categoria: Categoria? = null,
    val id_categoria: Long? = null
)
