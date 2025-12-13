package com.example.productos.data

data class UsuarioEntity(
    val id: Long,
    val nombre: String,
    val email: String,
    val clave: String,
    val rol: String?,
    val estado: String?
)