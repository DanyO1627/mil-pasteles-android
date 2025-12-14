package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.productos.screen.eve.LoginScreen
import com.example.productos.viewmodel.UsuarioViewModel
import org.junit.Rule
import org.junit.Test
import androidx.navigation.NavHostController
import io.mockk.mockk
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun login_muestra_textos_principales() {
        val navController = mockk<NavHostController>(relaxed = true)
        val usuarioViewModel = mockk<UsuarioViewModel>(relaxed = true)

        composeRule.setContent {
            LoginScreen(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        composeRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        composeRule.onNodeWithText("Correo electrónico").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("Entrar").assertIsDisplayed()
    }
}
