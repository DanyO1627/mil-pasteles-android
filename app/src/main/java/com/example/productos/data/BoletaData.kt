package com.example.productos.data

data class BoletaData(
    val numeroBoleta: Int,
    val nombre: String,
    val direccion: String,
    val region: String,
    val comuna: String,
    val referencia: String?,
    val fecha: String,
    val metodoPago: String,
    val items: List<CarritoItem>,
    val subtotal: Int,
    val envio: Int,
    val total: Int,

)
