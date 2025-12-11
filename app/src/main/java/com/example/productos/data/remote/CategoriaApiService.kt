package com.example.productos.data.remote

import com.example.productos.model.Categoria
import retrofit2.http.*

interface CategoriaApiService {

    @GET("categorias")
    suspend fun getAll(): List<Categoria>

    @GET("categorias/{id}")
    suspend fun getById(@Path("id") id: Long): Categoria

    @POST("categorias")
    suspend fun create(@Body categoria: Categoria): Categoria

    @PUT("categorias/{id}")
    suspend fun update(@Path("id") id: Long, @Body categoria: Categoria): Categoria

    @DELETE("categorias/{id}")
    suspend fun delete(@Path("id") id: Long)
}
