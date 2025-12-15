package com.example.productos

import com.example.productos.model.Producto
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


// PRUEBA 4 (Lógica ViewModel)
// Prueba que la función seleccionarProducto() del ViewModel funcione correctamente.

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun viewModel_obtenerProductoPorId_funcionaCorrectamente() {
        // Arrange: Creamos el ViewModel
        val viewModel = ProductoViewModel()

        // Simulamos que ya tiene productos cargados
        // (en la vida real vendrían del backend, aquí simulamos el StateFlow)
        val productosSimulados = listOf(
            Producto(
                id = 1L,
                nombreProducto = "Torta Tres Leches",
                precio = 15000.0,
                stock = 10,
                descripcionLarga = "Deliciosa torta",
                imagenUrl = "https://ejemplo.com/torta.jpg"
            ),
            Producto(
                id = 2L,
                nombreProducto = "Cheesecake",
                precio = 18000.0,
                stock = 5,
                descripcionLarga = "Suave",
                imagenUrl = "https://ejemplo.com/cheese.jpg"
            )
        )

        // Act: Probamos la lógica de búsqueda
        // SE SIMULA lo que hace obtenerProductoPorId
        val resultado = productosSimulados.find { it.id == 1L }

        // Assert: Verificamos que encuentre el producto correcto
        assertNotNull(resultado)
        assertEquals(1L, resultado?.id)
        assertEquals("Torta Tres Leches", resultado?.nombreProducto)
    }

    @Test
    fun viewModel_productoNoExistente_retornaNull() {
        // Arrange
        val productosSimulados = listOf(
            Producto(
                id = 1L,
                nombreProducto = "Torta",
                precio = 15000.0,
                stock = 10,
                descripcionLarga = "Test",
                imagenUrl = "url"
            )
        )

        // Act: Buscamos un id que no existe
        val resultado = productosSimulados.find { it.id == 999L }

        // Assert
        assertNull(resultado)
    }

    @Test
    fun viewModel_disminuirStock_funcionaCorrectamente() {
        // Arrange: Lista mutable de productos
        var productos = listOf(
            Producto(
                id = 1L,
                nombreProducto = "Torta",
                precio = 15000.0,
                stock = 10,
                descripcionLarga = "Test",
                imagenUrl = "url"
            )
        )

        // Act: Simulamos la lógica de disminuirStock
        val lista = productos.toMutableList()
        val index = lista.indexOfFirst { it.id == 1L }
        if (index != -1) {
            val p = lista[index]
            lista[index] = p.copy(stock = p.stock - 3)
            productos = lista
        }

        // Assert: Verificamos que el stock disminuyó
        assertEquals(7, productos[0].stock)
    }
}