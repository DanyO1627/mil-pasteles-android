package com.example.productos.remote

import com.example.productos.data.UsuarioEntity
import com.example.productos.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceLogin {

    @POST("/api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<UsuarioEntity>
}
