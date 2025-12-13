package com.example.productos.repository

import com.example.productos.model.LoginRequest
import com.example.productos.remote.RetrofitUsuario

class RepositorioLogin {

    suspend fun login(email: String, clave: String) =
        RetrofitUsuario.apiLogin.login(
            LoginRequest(
                email = email,
                clave = clave
            )
        )
}
