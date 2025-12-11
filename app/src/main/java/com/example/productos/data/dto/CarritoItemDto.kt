package com.example.productos.data.dto

data class CarritoItemDto(
    val productoId: Long,
    val nombreProducto: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val imagenUrl: String?
)