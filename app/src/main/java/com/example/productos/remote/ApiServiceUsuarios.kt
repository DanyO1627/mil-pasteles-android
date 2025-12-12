package com.example.productos.remote

import com.example.productos.data.UsuarioEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServiceUsuarios {

    @GET("/api/usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioEntity>>

    @POST("/api/usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioEntity): Response<UsuarioEntity>
}
