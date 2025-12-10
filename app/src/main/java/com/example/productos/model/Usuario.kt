package com.example.productos.model

data class Usuario(
    val nombre: String,
    val correo: String,
    val contraseña: String,
    val region: String,
    val comuna: String,
    // marca si es uno de los usuarios "semilla" permanentes
    val esPermanente: Boolean = false
)
