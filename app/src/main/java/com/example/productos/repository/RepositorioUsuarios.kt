package com.example.productos.repository

import com.example.productos.remote.RetrofitUsuario
import com.example.productos.remote.ApiServiceUsuarios


class RepositorioUsuarios {

    suspend fun obtenerUsuarioPorEmail(email: String)
            = RetrofitInstance.api.obtenerUsuarioPorEmail(email)

    suspend fun registrarUsuario(usuario: UsuarioEntity)
            = RetrofitInstance.api.registrarUsuario(usuario)
}