package com.example.productos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.productos.ui.utils.formatearPrecio
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.example.productos.data.CarritoItem
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCarrito(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    productoViewModel: ProductoViewModel
) {
    val lista by carritoViewModel.listaCarrito.collectAsState()
    val total by carritoViewModel.total.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA6B8),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },

        bottomBar = {
            if (lista.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Total: ${formatearPrecio(total)}",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { navController.navigate("compraExitosa") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF592D2D),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Ir a pagar")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFFFF8F9))
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {

            if (lista.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛒 Tu carrito está vacío", fontSize = 18.sp, color = Color.Gray)
                    }
                }
            } else {
                items(lista) { item ->
                    CarritoItemCard(
                        item = item,
                        carritoViewModel = carritoViewModel,
                        productoViewModel = productoViewModel
                    )
                }

                item {
                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            carritoViewModel.vaciarCarrito()
                            scope.launch { snackbarHostState.showSnackbar("Carrito vaciado") }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC1CC),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Vaciar carrito")
                    }
                }
            }
        }
    }
}

@Composable
fun CarritoItemCard(
    item: CarritoItem,
    carritoViewModel: CarritoViewModel,
    productoViewModel: ProductoViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.nombre,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, fontSize = 16.sp)
                Text("Precio: ${formatearPrecio(item.precio)}")

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { carritoViewModel.disminuirCantidad(item) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                        }

                        Text("${item.cantidad}", fontSize = 16.sp)

                        IconButton(onClick = { carritoViewModel.aumentarCantidad(item) }) {
                            Icon(Icons.Default.Add, contentDescription = "Aumentar")
                        }
                    }

                    Text(
                        "Subtotal: ${formatearPrecio(item.precio * item.cantidad)}",
                        fontSize = 14.sp
                    )
                }
            }

            IconButton(onClick = { carritoViewModel.eliminar(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
            }
        }
    }
}



