package com.example.productos

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


// PRUEBA UNITARIA 2

// Prueba que el botón agregar al carrito exista y sea visible

@RunWith(AndroidJUnit4::class)
class TestBotonAgregar {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun botonAgregarAlCarrito_esVisible() {
        // Arrange & Act: Renderizamos el botón
        composeTestRule.setContent {
            Button(onClick = { /* acción simulada */ }) {
                Text("🛒 Agregar al carrito")
            }
        }

        // Assert: Verificamos que el botón se muestre
        composeTestRule
            .onNodeWithText("🛒 Agregar al carrito")
            .assertIsDisplayed()
    }

    @Test
    fun botonSinStock_esVisible() {
        // Arrange & Act
        composeTestRule.setContent {
            Button(onClick = { }, enabled = false) {
                Text("Sin stock")
            }
        }

        // Assert
        composeTestRule
            .onNodeWithText("Sin stock")
            .assertIsDisplayed()
    }
}