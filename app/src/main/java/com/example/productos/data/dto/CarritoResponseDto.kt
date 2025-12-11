package com.example.productos.data.dto

data class CarritoResponseDto(
    val carritoId: Long,
    val items: List<CarritoItemDto>,
    val total: Double
)