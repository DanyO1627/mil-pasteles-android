package com.example.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.productos.screen.NavigationBarMain
import com.example.productos.ui.theme.PasteleriaMilSaboresTheme
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.CarritoViewModelFactory
import com.example.productos.viewmodel.ProductoViewModel

class MainActivity : ComponentActivity() {

    // ViewModels
    private val productoViewModel: ProductoViewModel by viewModels()

    private val carritoViewModel: CarritoViewModel by lazy {
        ViewModelProvider(this, CarritoViewModelFactory(productoViewModel))
            .get(CarritoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TOKEN MAPBOX
        com.mapbox.common.MapboxOptions.accessToken =
            "pk.eyJ1IjoiZnJlZGNhbXBvczEyMzAiLCJhIjoiY2xudTl2d2VrMDlpbzJrcWpnYnJkc3JqbCJ9.hjid1kkpkU37wvVJrj2pQg"

        setContent {

            // ---------- TEMA DE LA APP ----------
            PasteleriaMilSaboresTheme {

                LaunchedEffect(Unit) {
                    productoViewModel.cargarProductos()
                }

                // ---------- PROBAR APP COMPLETA ----------
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationBarMain(
                        viewModel = productoViewModel,
                        carritoViewModel = carritoViewModel
                    )
                }
            }
        }
    }
}
