package com.example.productos.repository

import com.example.productos.remote.RetrofitUsuario
import com.example.productos.data.UsuarioEntity
import retrofit2.Response

class RepositorioUsuarios {

    private val api = RetrofitUsuario.apiUsuarios

    suspend fun registrarUsuario(usuario: UsuarioEntity): Response<UsuarioEntity> {
        return api.registrarUsuario(usuario)
    }

    suspend fun listarUsuarios() = api.listarUsuarios()
}
