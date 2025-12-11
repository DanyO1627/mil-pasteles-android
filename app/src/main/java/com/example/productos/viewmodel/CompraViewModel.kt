package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.productos.data.CarritoItem
import com.example.productos.data.BoletaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompraViewModel : ViewModel() {

    private val _boletaActual = MutableStateFlow<BoletaData?>(null)
    val boletaActual = _boletaActual.asStateFlow()

    private var correlativo = 1

    fun generarBoleta(
        nombre: String,
        direccion: String,
        region: String,
        comuna: String,
        referencia: String?,
        metodoPago: String,
        items: List<CarritoItem>,
        envio: Int
    ) {
        val subtotal = items.sumOf { (it.precio * it.cantidad).toInt() }
        val total = subtotal + envio

        val fecha = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val boleta = BoletaData(
            numeroBoleta = correlativo,
            nombre = nombre,
            direccion = direccion,
            region = region,
            comuna = comuna,
            referencia = referencia,
            fecha = fecha,
            metodoPago = metodoPago,
            items = items.toList(),
            subtotal = subtotal,
            envio = envio,
            total = total
        )

        correlativo += 1
        _boletaActual.value = boleta
    }
}
