package com.example.productos

import com.example.productos.data.dto.AgregarItemRequestDto
import com.example.productos.data.dto.CarritoItemDto
import com.example.productos.data.dto.CarritoResponseDto
import com.example.productos.remote.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import com.example.productos.repository.CarritoRepository

class CarritoRepositoryTest {

    private lateinit var repository: CarritoRepository
    private lateinit var mockApi: ApiService

    @Before
    fun setup() {
        mockApi = mockk()
        repository = CarritoRepository(mockApi)
    }


    @Test
    fun crearCarrito_devuelve_success_cuando_la_respuesta_es_exitosa() = runTest {
        // Given
        val carritoResponse = CarritoResponseDto(
            carritoId = 1L,
            items = emptyList(),
            total = 0.0
        )
        coEvery { mockApi.crearCarrito() } returns Response.success(carritoResponse)

        // When
        val result = repository.crearCarrito()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull()?.carritoId)
        assertEquals(0.0, result.getOrNull()?.total?: 0.0,0.0)
        coVerify(exactly = 1) { mockApi.crearCarrito() }
    }

    @Test
    fun crearCarrito_devuelve_failure_cuando_la_respuesta_no_es_exitosa() = runTest {
        // Given
        coEvery { mockApi.crearCarrito() } returns Response.error(
            500,
            "Error".toResponseBody()
        )

        // When
        val result = repository.crearCarrito()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Error al crear carrito") == true)
    }

    @Test
    fun obtenerCarrito_devuelve_success_cuando_encuentra_el_carrito() = runTest {
        val carritoId = 1L
        val item = CarritoItemDto(
            productoId = 10L,
            nombreProducto = "Pastel de Chocolate",
            precioUnitario = 15000.0,
            cantidad = 2,
            imagenUrl = "http://example.com/pastel.jpg"
        )
        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = listOf(item),
            total = 30000.0
        )
        coEvery { mockApi.obtenerCarrito(carritoId) } returns Response.success(carritoResponse)

        val result = repository.obtenerCarrito(carritoId)

        assertTrue(result.isSuccess)
        assertEquals(1L, result.getOrNull()?.carritoId)
        assertEquals(1, result.getOrNull()?.items?.size)
        assertEquals(30000.0, result.getOrNull()?.total?: 0.0,0.0)
        coVerify(exactly = 1) { mockApi.obtenerCarrito(carritoId) }
    }

    @Test
    fun agregarItem_agrega_producto_correctamente() = runTest {

        val carritoId = 1L
        val productoId = 10L
        val cantidad = 2
        val request = AgregarItemRequestDto(productoId, cantidad)

        val item = CarritoItemDto(
            productoId = productoId,
            nombreProducto = "Pastel de Chocolate",
            precioUnitario = 15000.0,
            cantidad = cantidad,
            imagenUrl = null
        )
        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = listOf(item),
            total = 30000.0
        )

        coEvery { mockApi.agregarItem(carritoId, request) } returns Response.success(carritoResponse)


        val result = repository.agregarItem(carritoId, productoId, cantidad)


        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.items?.size)
        assertEquals(productoId, result.getOrNull()?.items?.first()?.productoId)
        assertEquals(cantidad, result.getOrNull()?.items?.first()?.cantidad)
        coVerify(exactly = 1) { mockApi.agregarItem(carritoId, request) }
    }

    @Test
    fun disminuirItem_disminuye_cantidad_correctamente() = runTest {

        val carritoId = 1L
        val productoId = 10L

        val item = CarritoItemDto(
            productoId = productoId,
            nombreProducto = "Pastel de Chocolate",
            precioUnitario = 15000.0,
            cantidad = 1,
            imagenUrl = null
        )
        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = listOf(item),
            total = 15000.0
        )

        coEvery { mockApi.disminuirItem(carritoId, productoId) } returns Response.success(carritoResponse)


        val result = repository.disminuirItem(carritoId, productoId)


        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.items?.first()?.cantidad)
        assertEquals(15000.0, result.getOrNull()?.total?: 0.0,0.0)
        coVerify(exactly = 1) { mockApi.disminuirItem(carritoId, productoId) }
    }


    @Test
    fun eliminarItem_elimina_producto_correctamente() = runTest {

        val carritoId = 1L
        val productoId = 10L

        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = emptyList(), // producto eliminado
            total = 0.0
        )

        coEvery { mockApi.eliminarItem(carritoId, productoId) } returns Response.success(carritoResponse)

        val result = repository.eliminarItem(carritoId, productoId)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.items?.isEmpty() == true)
        assertEquals(0.0, result.getOrNull()?.total?: 0.0,0.0)
        coVerify(exactly = 1) { mockApi.eliminarItem(carritoId, productoId) }
    }


    @Test
    fun vaciarCarrito_elimina_todos_los_items() = runTest {

        val carritoId = 1L
        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = emptyList(),
            total = 0.0
        )

        coEvery { mockApi.vaciarCarrito(carritoId) } returns Response.success(carritoResponse)

        val result = repository.vaciarCarrito(carritoId)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.items?.isEmpty() == true)
        assertEquals(0.0, result.getOrNull()?.total?: 0.0,0.0)
        coVerify(exactly = 1) { mockApi.vaciarCarrito(carritoId) }
    }


    @Test
    fun confirmarCompra_confirma_la_compra_exitosamente() = runTest {

        val carritoId = 1L
        val carritoResponse = CarritoResponseDto(
            carritoId = carritoId,
            items = emptyList(),
            total = 0.0
        )

        coEvery { mockApi.confirmarCompra(carritoId) } returns Response.success(carritoResponse)


        val result = repository.confirmarCompra(carritoId)


        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { mockApi.confirmarCompra(carritoId) }
    }

}