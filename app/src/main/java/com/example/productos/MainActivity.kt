//package com.example.productos
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.viewModels // Nuevo para el ViewModel
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//
//
//import androidx.room.Room
//import com.example.productos.viewmodel.ProductoViewModel
//
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.core.content.ContextCompat.getSystemService
//import com.example.productos.ui.theme.PasteleriaMilSaboresTheme
//import com.example.productos.R
//
//
//
//import com.example.productos.screen.NavigationBarMain
//import com.example.productos.screen.ScreenProductos
//
//import kotlin.getValue
//import com.example.productos.viewmodel.CarritoViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.productos.screen.ScreemProbandoCositas
//import com.example.productos.viewmodel.CarritoViewModelFactory
//
//
//class
//MainActivity : ComponentActivity() {
//
//    // ViewModels
//    private val productoViewModel: ProductoViewModel by viewModels()
//    private val carritoViewModel: CarritoViewModel by lazy { // pasandole los productos de viewmodel
//        ViewModelProvider(this, CarritoViewModelFactory(productoViewModel))
//            .get(CarritoViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        // CONFIGURAR TOKEN DE MAPBOX PRIMERO (ANTES DE SETCONTENT)
//        com.mapbox.common.MapboxOptions.accessToken =
//            "pk.eyJ1IjoiZnJlZGNhbXBvczEyMzAiLCJhIjoiY2xudTl2d2VrMDlpbzJrcWpnYnJkc3JqbCJ9.hjid1kkpkU37wvVJrj2pQg"
//
////        setContent {
////            MaterialTheme {
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    NavigationBarMain(
////                        viewModel = productoViewModel,
////                        carritoViewModel = carritoViewModel)
////                }
////            }
//        setContent {
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    // 👉 SOLO pantalla de prueba
//                    ScreemProbandoCositas(productoViewModel)
//                }
//            }
//        }
//    }
//}
//
//
//
//
//


package com.example.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.productos.ui.screens.ScreemProbandoCositas
import com.example.productos.ui.theme.PasteleriaMilSaboresTheme
import com.example.productos.viewmodel.ProductoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Crear el ViewModel AQUÍ, una sola vez
        val productoViewModel = ViewModelProvider(this)[ProductoViewModel::class.java]

        setContent {
            PasteleriaMilSaboresTheme {
                // ✅ Pasar el ViewModel creado
                ScreemProbandoCositas(viewModel = productoViewModel)
            }
        }
    }
}