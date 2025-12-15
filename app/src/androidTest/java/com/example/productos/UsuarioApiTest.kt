package com.example.productos

import com.example.productos.data.UsuarioEntity
import com.example.productos.remote.ApiServiceUsuarios
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class UsuarioApiTest {
    private val api = mockk<ApiServiceUsuarios>()


    @Test
    fun obtenerUsuarioPorEmail_retorna_usuario() = runTest {


        val email = "admin@milsabores.cl"


        val usuarioMock = UsuarioEntity(
            id = 1,
            nombre = "Admin",
            email = email,
            clave = "empleado123",
            region = "Región Metropolitana",
            comuna = "Santiago"
        )


        coEvery {
            api.obtenerUsuarioPorEmail(email)
        } returns Response.success(usuarioMock)


        val response = api.obtenerUsuarioPorEmail(email)


        assertEquals(true, response.isSuccessful)
        assertEquals("Admin", response.body()?.nombre)
        assertEquals("Región Metropolitana", response.body()?.region)
    }
}

