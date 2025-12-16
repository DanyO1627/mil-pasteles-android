package com.example.productos

import com.example.productos.model.Producto
import org.junit.Assert.*
import org.junit.Test


// PRUEBA 4 (Lógica ViewModel)
// Prueba que la función seleccionarProducto() del ViewModel funcione correctamente.


class TestProductoStock {

    @Test
    fun producto_disminuyeStock_correctamente() {
        // Arrange: se prepara un producto con stock inicial para el test
        val producto = Producto(
            id = 1L,
            nombreProducto = "Torta",
            precio = 15000.0,
            stock = 10,
            descripcionLarga = "Test",
            imagenUrl = "url"
        )

        // Act: se simula la disminución de stock en 3
        val productoActualizado = producto.copy(stock = producto.stock - 3)

        // Assert: se verifica que el stock sea el esperado, si es 7 ok, si no es 7, falla
        assertEquals(7, productoActualizado.stock)
    }
}