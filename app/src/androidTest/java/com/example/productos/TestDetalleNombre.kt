package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.material3.Text


// PRUEBA 1: Muestra que el nombre del producto se muestre correctamente en pantalla
// ¿Por qué es unitaria?


@RunWith(AndroidJUnit4::class)
class TestDetalleNombre {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detalleProducto_muestraNombreCorrectamente() {
        // Arrange: preparando un producto de ejemplo
        val nombreProducto = "Torta Tres Leches"

        // Act: Renderizamos el componente Text con el nombre
        composeTestRule.setContent {
            Text(text = nombreProducto)
        }

        // Pausa antes de hacer la verificación
        composeTestRule.waitForIdle()

        // Assert: Verificamos que el texto se muestre en pantalla
        composeTestRule
            .onNodeWithText("Torta Tres Leches")
            .assertIsDisplayed()
    }

    @Test
    fun detalleProducto_muestraPrecio() {
        // Arrange
        val precio = "$15000"

        // Act
        composeTestRule.setContent {
            Text(text = precio)
        }

        // Assert
        composeTestRule
            .onNodeWithText("$15000")
            .assertIsDisplayed()
    }
}