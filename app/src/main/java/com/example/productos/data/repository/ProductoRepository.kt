package com.example.productos.data.repository

import com.example.productos.data.remote.ProductoApiService
import com.example.productos.model.Producto

class ProductoRepository(private val api: ProductoApiService) {

    // llama a get all, eso es lo correcto
    suspend fun getAll(): List<Producto> = api.getAll()

    suspend fun getById(id: Long): Producto = api.getById(id)

    suspend fun create(producto: Producto): Producto = api.create(producto)

    suspend fun update(id: Long, producto: Producto): Producto =
        api.update(id, producto)

    suspend fun delete(id: Long) = api.delete(id)
}