package com.example.productos.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.productos.viewmodel.ProductoViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.example.productos.model.Producto
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import com.example.productos.model.Categoria



@Composable
fun CategoriaTestScreen(productoViewModel: ProductoViewModel) {

    val productos by productoViewModel.listaProductos.collectAsState()

    // categoría seleccionada
    var categoriaSeleccionada by remember { mutableStateOf<Long?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Prueba de Categorías", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton("Tortas", 1) { categoriaSeleccionada = it }
            CategoryButton("Tartas", 2) { categoriaSeleccionada = it }
            CategoryButton("Galletas", 3) { categoriaSeleccionada = it }
            CategoryButton("Postres", 4) { categoriaSeleccionada = it }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --------------------------
        //  FILTRO ESTABLE Y SEGURO
        // --------------------------

        val filtrados = remember(productos, categoriaSeleccionada) {

            if (categoriaSeleccionada == null) {
                productos  // muestra todo
            } else {
                productos.filter { prod ->
                    prod.id_categoria != null && prod.id_categoria == categoriaSeleccionada
                }
            }
        }

        // Si NO hay productos todavía → se muestra algo, NO queda en blanco
        if (productos.isEmpty()) {
            Text("Cargando productos...", modifier = Modifier.padding(16.dp))
            return
        }

        // Si el filtro queda vacío → mostrar mensaje, no pantalla blanca
        if (filtrados.isEmpty()) {
            Text("Sin productos en esta categoría.", modifier = Modifier.padding(16.dp))
            return
        }

        LazyColumn {
            items(filtrados) { producto ->
                Text(
                    text = "${producto.nombreProducto} — $${producto.precio}",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryButton(texto: String, categoria: Long, onClick: (Long) -> Unit) {
    Button(onClick = { onClick(categoria) }) {
        Text(texto)
    }
}
