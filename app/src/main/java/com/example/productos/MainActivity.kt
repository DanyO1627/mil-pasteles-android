package com.example.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import kotlinx.coroutines.launch

import com.example.productos.viewmodel.ProductoViewModel
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.screen.NavigationBarMain
// ✨ NUEVOS IMPORTS PARA BACKEND
import com.example.productos.remote.RetrofitInstance
import com.example.productos.repository.CarritoRepository

class MainActivity : ComponentActivity() {
    // ViewModels
    private val productoViewModel: ProductoViewModel by viewModels()

    // ✨ NUEVO: Crear repositorio para backend
    private val carritoRepository by lazy {
        CarritoRepository(RetrofitInstance.api)
    }

    // 🔄 ACTUALIZADO: CarritoViewModel ahora usa el repositorio del backend
    private val carritoViewModel: CarritoViewModel by lazy {
        ViewModelProvider(
            this,
            CarritoViewModelFactory(carritoRepository, applicationContext)
        ).get(CarritoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // CONFIGURAR TOKEN DE MAPBOX PRIMERO (ANTES DE SETCONTENT)
        com.mapbox.common.MapboxOptions.accessToken = "pk.eyJ1IjoiZnJlZGNhbXBvczEyMzAiLCJhIjoiY2xudTl2d2VrMDlpbzJrcWpnYnJkc3JqbCJ9.hjid1kkpkU37wvVJrj2pQg"

        setContent {
            MaterialTheme {
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

        // ✨ NUEVO: Observar errores del carrito y mostrarlos como Toast
        lifecycleScope.launch {
            carritoViewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(
                        this@MainActivity,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                    carritoViewModel.limpiarError()
                }
            }
        }
    }
}





