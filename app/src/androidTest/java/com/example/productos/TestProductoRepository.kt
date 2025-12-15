package com.example.productos

import com.example.productos.data.remote.ProductoApiService
import com.example.productos.data.repository.ProductoRepository
import com.example.productos.model.Producto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TestProductoRepository {

    @Test
    fun repository_obtieneProductosCorrectamente() = runTest {
        // Arrange: Creamos un mock de la API
        val apiMock = mockk<ProductoApiService>()

        // Productos fake que simula el backend
        val productosFake = listOf(
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
                nombreProducto = "Cheesecake de Frutilla",
                precio = 18000.0,
                stock = 5,
                descripcionLarga = "Suave y cremoso",
                imagenUrl = "https://ejemplo.com/cheese.jpg"
            )
        )

        // se configura el mock: cuando se llame a getall(), devolver productosFake
        coEvery { apiMock.getAll() } returns productosFake

        // Act: se crea el repository con el mock y llamamos al método
        val repository = ProductoRepository(apiMock)
        val resultado = repository.getAll()

        // Assert: se verifica que devuelva los productos correctos
        Assert.assertEquals(2, resultado.size)
        Assert.assertEquals("Torta Tres Leches", resultado[0].nombreProducto)
        Assert.assertEquals(15000.0, resultado[0].precio, 0.01)
        Assert.assertEquals("Cheesecake de Frutilla", resultado[1].nombreProducto)
    }

    @Test
    fun repository_obtieneProductoPorId() = runTest {
        // Arrange
        val apiMock = mockk<ProductoApiService>()
        val productoFake = Producto(
            id = 5L,
            nombreProducto = "Brownie de Chocolate",
            precio = 8000.0,
            stock = 20,
            descripcionLarga = "Intenso y húmedo",
            imagenUrl = "https://ejemplo.com/brownie.jpg"
        )

        coEvery { apiMock.getById(5L) } returns productoFake

        // Act
        val repository = ProductoRepository(apiMock)
        val resultado = repository.getById(5L)

        // Assert
        Assert.assertNotNull(resultado)
        Assert.assertEquals(5L, resultado.id)
        Assert.assertEquals("Brownie de Chocolate", resultado.nombreProducto)
        Assert.assertEquals(8000.0, resultado.precio, 0.01)
    }
}