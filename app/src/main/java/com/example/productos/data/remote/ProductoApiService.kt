package com.example.productos.data.remote

import com.example.productos.model.Producto
import retrofit2.http.*

interface ProductoApiService {

    @GET("productos")
    suspend fun getAll(): List<Producto>

    @GET("productos/{id}")
    suspend fun getById(@Path("id") id: Long): Producto

    @POST("productos")
    suspend fun create(@Body producto: Producto): Producto

    @PUT("productos/{id}")
    suspend fun update(@Path("id") id: Long, @Body producto: Producto): Producto

    @DELETE("productos/{id}")
    suspend fun delete(@Path("id") id: Long)
}
