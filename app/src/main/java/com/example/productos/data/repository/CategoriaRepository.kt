package com.example.productos.data.repository

import com.example.productos.data.remote.CategoriaApiService
import com.example.productos.model.Categoria

class CategoriaRepository(
    private val api: CategoriaApiService
) {
    suspend fun getAll(): List<Categoria> = api.getAll()

    suspend fun getById(id: Long): Categoria = api.getById(id)

    suspend fun create(categoria: Categoria): Categoria = api.create(categoria)

    suspend fun update(id: Long, categoria: Categoria): Categoria = api.update(id, categoria)

    suspend fun delete(id: Long) = api.delete(id)
}
