package com.example.productos.remote

import com.example.productos.data.UsuarioEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceUsuarios {

    @GET("/usuarios/{email}")
    suspend fun obtenerUsuarioPorEmail(
        @Path("email") email: String
    ): Response<UsuarioEntity>

    @POST("/usuarios")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioEntity
    ): Response<UsuarioEntity>
}