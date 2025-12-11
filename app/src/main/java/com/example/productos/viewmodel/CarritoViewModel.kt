package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.productos.data.CarritoItem
import com.example.productos.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarritoViewModel(
    private val productoViewModel: ProductoViewModel
) : ViewModel() {

    private val _listaCarrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val listaCarrito = _listaCarrito.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total = _total.asStateFlow()

    fun agregarAlCarrito(producto: Producto) {
        val lista = _listaCarrito.value.toMutableList()

        val stockDisponible = producto.stock
        if (stockDisponible <= 0) return

        val existente = lista.find { it.id == producto.id }

        if (existente != null) {
            val nuevo = existente.copy(cantidad = existente.cantidad + 1)
            lista[lista.indexOf(existente)] = nuevo
        } else {
            lista.add(
                CarritoItem(
                    id = producto.id!!,
                    nombre = producto.nombreProducto,
                    precio = producto.precio,
                    imagenUrl = producto.imagenUrl,
                    cantidad = 1
                )
            )
        }

        productoViewModel.disminuirStock(producto.id, 1)

        _listaCarrito.value = lista
        _total.value = lista.sumOf { it.precio * it.cantidad }
    }

    fun aumentarCantidad(item: CarritoItem) {
        val p = productoViewModel.obtenerProductoPorId(item.id)
        if (p?.stock ?: 0 <= 0) return

        val lista = _listaCarrito.value.toMutableList()
        val i = lista.indexOfFirst { it.id == item.id }

        if (i != -1) {
            lista[i] = item.copy(cantidad = item.cantidad + 1)
            _listaCarrito.value = lista

            productoViewModel.disminuirStock(item.id, 1)
            _total.value = lista.sumOf { it.precio * it.cantidad }
        }
    }

    fun disminuirCantidad(item: CarritoItem) {
        val lista = _listaCarrito.value.toMutableList()
        val i = lista.indexOfFirst { it.id == item.id }

        if (i != -1) {
            val ex = lista[i]

            if (ex.cantidad > 1) {
                lista[i] = ex.copy(cantidad = ex.cantidad - 1)
            } else {
                lista.removeAt(i)
            }

            productoViewModel.aumentarStock(item.id, 1)
            _listaCarrito.value = lista
            _total.value = lista.sumOf { it.precio * it.cantidad }
        }
    }

    fun eliminar(item: CarritoItem) {
        val lista = _listaCarrito.value.toMutableList()
        val removed = lista.find { it.id == item.id } ?: return

        lista.removeAll { it.id == item.id }
        productoViewModel.aumentarStock(item.id, removed.cantidad)

        _listaCarrito.value = lista
        _total.value = lista.sumOf { it.precio * it.cantidad }
    }

    fun vaciarCarrito() {
        _listaCarrito.value = emptyList()
        _total.value = 0.0
    }

    fun confirmarCompra() {
        _listaCarrito.value = emptyList()
        _total.value = 0.0
    }
}
