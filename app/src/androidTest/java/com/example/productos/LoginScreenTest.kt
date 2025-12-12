package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.productos.screen.eve.LoginScreen
import com.example.productos.viewmodel.UsuarioViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun login_muestra_campos_y_boton() {

        composeRule.setContent {
            val navController = rememberNavController()
            val usuarioViewModel = UsuarioViewModel()

            LoginScreen(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        // Campo email (label)
        composeRule
            .onNodeWithText("Correo electrónico")
            .assertIsDisplayed()

        // Campo contraseña (label)
        composeRule
            .onNodeWithText("Contraseña")
            .assertIsDisplayed()

        // Botón
        composeRule
            .onNodeWithText("Entrar")
            .assertIsDisplayed()
    }
}
