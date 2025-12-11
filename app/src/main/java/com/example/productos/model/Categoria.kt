package com.example.productos.model

data class Categoria(
    val id: Long? = null,
    val nombreCategoria: String = "",
    val descripcionCategoria: String? = "",
    val imagenUrl: String? = "",
    val activo: Boolean? = true
)