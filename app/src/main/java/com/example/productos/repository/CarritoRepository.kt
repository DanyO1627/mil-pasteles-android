package com.example.productos.repository

import com.example.productos.data.dto.AgregarItemRequestDto
import com.example.productos.data.dto.CarritoResponseDto
import com.example.productos.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarritoRepository(private val api: ApiService) {

    // Crear carrito nuevo
    suspend fun crearCarrito(): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.crearCarrito()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear carrito: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener carrito existente
    suspend fun obtenerCarrito(carritoId: Long): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerCarrito(carritoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener carrito: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Agregar producto al carrito
    suspend fun agregarItem(
        carritoId: Long,
        productoId: Long,
        cantidad: Int
    ): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val request = AgregarItemRequestDto(productoId, cantidad)
            val response = api.agregarItem(carritoId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al agregar item: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Disminuir cantidad de producto
    suspend fun disminuirItem(
        carritoId: Long,
        productoId: Long
    ): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.disminuirItem(carritoId, productoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al disminuir item: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Eliminar producto del carrito
    suspend fun eliminarItem(
        carritoId: Long,
        productoId: Long
    ): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.eliminarItem(carritoId, productoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al eliminar item: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Vaciar carrito completo
    suspend fun vaciarCarrito(carritoId: Long): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.vaciarCarrito(carritoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al vaciar carrito: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Confirmar compra
    suspend fun confirmarCompra(carritoId: Long): Result<CarritoResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = api.confirmarCompra(carritoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al confirmar compra: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}