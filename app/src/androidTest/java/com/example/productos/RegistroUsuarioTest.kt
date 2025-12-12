package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.productos.screen.eve.ScreenRegistroUsuario
import com.example.productos.viewmodel.UsuarioViewModel
import org.junit.Rule
import org.junit.Test

class RegistroUsuarioTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun registro_muestra_campos_principales() {

        composeRule.setContent {
            val navController = rememberNavController()
            val usuarioViewModel = UsuarioViewModel()

            ScreenRegistroUsuario(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        // ✅ Cambia estos textos por los EXACTOS que ves en tu UI
        composeRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña").assertIsDisplayed()

        // Botón
        composeRule.onNodeWithText("Registrarse").assertIsDisplayed()
        // o si tu botón dice "Registrar" / "Crear cuenta", usa ese texto exacto
    }
}
