package com.example.productos.data

data class UsuarioEntity(
    val id: Long? = null,
    val nombre: String,
    val email: String,
    val clave: String,
    val region: String,
    val comuna: String
)