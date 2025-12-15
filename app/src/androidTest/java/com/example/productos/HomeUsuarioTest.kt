package com.example.productos

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.productos.remote.RetrofitInstance
import com.example.productos.repository.CarritoRepository
import com.example.productos.screen.eve.PantallaTienda
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import org.junit.Rule
import org.junit.Test

class HomeUsuarioTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun pantalla_tienda_muestra_titulo_principal() {
        // Obtener el contexto de la aplicación de prueba
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.setContent {
            val navController = rememberNavController()

            // Crear ProductoViewModel
            val productoViewModel = ProductoViewModel()

            // Crear CarritoRepository con la API de Retrofit
            val apiService = RetrofitInstance.api
            val carritoRepository = CarritoRepository(apiService)
            val carritoViewModel = CarritoViewModel(carritoRepository, context)

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