package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productos.data.CarritoItem
import com.example.productos.data.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow




class CarritoViewModelFactory(
    private val productoViewModel: ProductoViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(productoViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class CarritoViewModel(
    private val productoViewModel: ProductoViewModel // esto inserta los productos desde el view model
) : ViewModel() {

    private val _listaCarrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val listaCarrito = _listaCarrito.asStateFlow()
    private val _total = MutableStateFlow(0)
    val total = _total.asStateFlow()



    fun agregarAlCarrito(producto: Producto) {
        val actual = _listaCarrito.value.toMutableList()
        val restante = productoViewModel.obtenerProductoPorId(producto.id)?.stock ?: 0
        if (restante <= 0) {
            println("❌ Sin stock para ${producto.nombre}")
            return
        }

        val existente = actual.find { it.id == producto.id }
        if (existente != null) {
            actual[actual.indexOf(existente)] = existente.copy(cantidad = existente.cantidad + 1)
        } else {
            actual.add(
                CarritoItem(
                    id = producto.id,
                    nombre = producto.nombre,
                    precio = producto.precio,
                    imagen = producto.imagen,
                    cantidad = 1
                )
            )
        }
        _listaCarrito.value = actual

        // stock total baja de a uno
        productoViewModel.disminuirStock(producto.id, 1)

        // recalcula el total automáticamente
        _total.value = _listaCarrito.value.sumOf { it.precio * it.cantidad }
    }

    fun aumentarCantidad(item: CarritoItem) {
        val restante = productoViewModel.obtenerProductoPorId(item.id)?.stock ?: 0
        if (restante <= 0) {
            println("⚠️ Stock máximo alcanzado para ${item.nombre}")
            return
        }
        val lista = _listaCarrito.value.toMutableList()
        val i = lista.indexOfFirst { it.id == item.id }
        if (i != -1) {
            lista[i] = item.copy(cantidad = item.cantidad + 1)
            _listaCarrito.value = lista
            // baja stock global por 1
            productoViewModel.disminuirStock(item.id, 1)

            // de nuevo recalcula el total
            _total.value = _listaCarrito.value.sumOf { it.precio * it.cantidad }
        }
    }

    fun disminuirCantidad(item: CarritoItem) {
        val lista = _listaCarrito.value.toMutableList()
        val i = lista.indexOfFirst { it.id == item.id }
        if (i != -1) {
            val existente = lista[i]
            if (existente.cantidad > 1) {
                lista[i] = existente.copy(cantidad = existente.cantidad - 1)
            } else {
                lista.removeAt(i)
            }
            _listaCarrito.value = lista
            // devuelve 1 al stock global
            productoViewModel.aumentarStock(item.id, 1)

            // vuelve a recalcular el total
            _total.value = _listaCarrito.value.sumOf { it.precio * it.cantidad }
        }
    }

    fun eliminar(item: CarritoItem) {
        val lista = _listaCarrito.value.toMutableList()
        val removed = lista.firstOrNull { it.id == item.id } ?: return
        lista.removeAll { it.id == item.id }
        _listaCarrito.value = lista
        // devuelve TODA la cantidad al stock global
        productoViewModel.aumentarStock(item.id, removed.cantidad)

        // vuelve a recalcular
        _total.value = _listaCarrito.value.sumOf { it.precio * it.cantidad }
    }

    fun vaciarCarrito() {
        // devuelve stock a inventario
        _listaCarrito.value.forEach { productoViewModel.aumentarStock(it.id, it.cantidad) }
        _listaCarrito.value = emptyList()

        // el total vuelve a 0
        _total.value = 0
    }

//    fun calcularTotal(): Int = _listaCarrito.value.sumOf { it.precio * it.cantidad }

    fun confirmarCompra() {
        // esta solo limpia el carrito, para que no se reinicie el stock cuando se haga la compra
        // CAMBIO IMPORTANTE
        _listaCarrito.value = emptyList()

        // después de comprar, el total vuelve a 0
        _total.value = 0
    }



}





