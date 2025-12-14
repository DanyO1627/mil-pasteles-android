package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.productos.screen.eve.ScreenRegistroUsuario
import com.example.productos.viewmodel.UsuarioViewModel
import org.junit.Rule
import org.junit.Test
import io.mockk.mockk
class RegistroUsuarioTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun registro_muestra_campos_principales() {
        val navController = mockk<NavController>(relaxed = true)
        val usuarioViewModel = mockk<UsuarioViewModel>(relaxed = true)

        composeRule.setContent {
            ScreenRegistroUsuario(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }

        composeRule.onNodeWithText("Registro de Usuario").assertIsDisplayed()
        composeRule.onNodeWithText("Nombre").assertIsDisplayed()
        composeRule.onNodeWithText("Correo").assertIsDisplayed()
        composeRule.onNodeWithText("Contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("Confirmar contraseña").assertIsDisplayed()
        composeRule.onNodeWithText("Registrar").assertIsDisplayed()
    }
}
