package com.example.productos.repository

import com.example.productos.data.UsuarioEntity
import com.example.productos.remote.RetrofitUsuario

class RepositorioUsuarios {

    private val api = RetrofitUsuario.apiUsuarios

    suspend fun obtenerUsuarioPorEmail(email: String) =
        api.obtenerUsuarioPorEmail(email)

    suspend fun registrarUsuario(usuario: UsuarioEntity) =
        api.registrarUsuario(usuario)
}