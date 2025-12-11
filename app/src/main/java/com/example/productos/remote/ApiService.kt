package com.example.productos.remote

import com.example.productos.model.Producto
import com.example.productos.data.dto.AgregarItemRequestDto
import com.example.productos.data.dto.CarritoResponseDto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // ========== ENDPOINTS DE CARRITO ==========

    // Crear un nuevo carrito
    @POST("api/carrito")
    suspend fun crearCarrito(): Response<CarritoResponseDto>

    // Obtener carrito por ID
    @GET("api/carrito/{id}")
    suspend fun obtenerCarrito(@Path("id") carritoId: Long): Response<CarritoResponseDto>

    // Agregar item al carrito
    @POST("api/carrito/{id}/items")
    suspend fun agregarItem(
        @Path("id") carritoId: Long,
        @Body request: AgregarItemRequestDto
    ): Response<CarritoResponseDto>

    // Disminuir cantidad de un item
    @PATCH("api/carrito/{id}/items/{productoId}/disminuir")
    suspend fun disminuirItem(
        @Path("id") carritoId: Long,
        @Path("productoId") productoId: Long
    ): Response<CarritoResponseDto>

    // Eliminar item del carrito
    @DELETE("api/carrito/{id}/items/{productoId}")
    suspend fun eliminarItem(
        @Path("id") carritoId: Long,
        @Path("productoId") productoId: Long
    ): Response<CarritoResponseDto>

    // Vaciar carrito completo
    @DELETE("api/carrito/{id}")
    suspend fun vaciarCarrito(@Path("id") carritoId: Long): Response<CarritoResponseDto>

    // Confirmar compra
    @POST("api/carrito/{id}/confirmar")
    suspend fun confirmarCompra(@Path("id") carritoId: Long): Response<CarritoResponseDto>
}