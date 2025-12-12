package com.example.productos.data

data class UsuarioEntity(
    val id: Long? = null,
    val nombre: String,
    val email: String,
    val edad: Int? = null,
    val clave: String,
    val region: String,
    val comuna: String,
    val estado: String = "Pendiente",
    val rol: String = "cliente",
    val categoria: String = "base",
    val fecha: String = ""
)
