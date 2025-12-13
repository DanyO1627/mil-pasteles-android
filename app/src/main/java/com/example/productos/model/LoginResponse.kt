package com.example.productos.model

import com.example.productos.data.UsuarioEntity   // ← AGREGA ESTO

data class LoginResponse(
    val mensaje: String,
    val token: String?,
    val usuario: UsuarioEntity?
)
