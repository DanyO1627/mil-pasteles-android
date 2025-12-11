package com.example.productos.ui.utils

import java.text.NumberFormat
import java.util.Locale

// función ÚNICA para todo el proyecto
fun formatearPrecio(monto: Number): String {
    val entero = monto.toLong() // acepta Int, Double, etc.
    val formato = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formato.minimumFractionDigits = 0
    return formato.format(entero) // ej: $12.990
}