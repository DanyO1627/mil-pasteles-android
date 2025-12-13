package com.example.productos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.productos.screen.eve.PantallaTienda
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import org.junit.Rule
import org.junit.Test

class HomeUsuarioTest{

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun pantalla_tienda_muestra_titulo_principal() {

        composeRule.setContent {
            val navController = rememberNavController()

            val productoViewModel = ProductoViewModel()
            val carritoViewModel = CarritoViewModel(productoViewModel)

            PantallaTienda(
                navController = navController,
                viewModel = productoViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composeRule
            .onNodeWithText("Pastelería Mil Sabores")
            .assertIsDisplayed()
    }
}
