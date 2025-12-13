package com.example.productos.remote


import com.example.productos.data.UsuarioEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import androidx.camera.core.ImageProcessor.Response
import retrofit2.http.Body

interface ApiServiceUsuarios {

    @GET("usuarios/{email}")
    suspend fun obtenerUsuarioPorEmail(
        @Path("email") email: String
    ): Response<UsuarioEntity>

    @POST("usuarios")
    suspend fun registrarUsuario(
        @Body usuario: UsuarioEntity
    ): Response<UsuarioEntity>
}