package com.example.productos.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.productos.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreemProbandoCositas(
    viewModel: ProductoViewModel
) {
    val productos by viewModel.productos.collectAsState()

    // ✅ Agregar log para ver el estado
    LaunchedEffect(productos.size) {
        Log.d("ScreenPrueba", "Productos en pantalla: ${productos.size}")
    }

    LaunchedEffect(Unit) {
        Log.d("ScreenPrueba", "Iniciando carga de productos...")
        viewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prueba de Productos") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Total productos: ${productos.size}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (productos.isEmpty()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        "Cargando productos...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Si esto no cambia, revisa Logcat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = producto.nombreProducto,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Precio: $${producto.precio}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Stock: ${producto.stock}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "ID: ${producto.id}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}