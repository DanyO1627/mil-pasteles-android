
// qué hace el código:
//Lee el producto seleccionado desde el ViewModel
//Muestra su imagen, nombre, precio y descripción
//Incluye botones + y – para elegir cantidad
//Botón Agregar al carro (provisoriamente hace un sout)
// puede volver atrás con el ícono del menú superior.

package com.example.productos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.productos.ui.utils.vibrarSuave
import com.example.productos.viewmodel.ProductoViewModel
import com.example.productos.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDetalleProducto(
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    // Contexto y vibración
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    fun triggerHapticFeedbackSuave() {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        vibrarSuave(context) // VIBRA AL AGREGAR
    }

    // el producto seleccionado
    val productoSeleccionado by viewModel.productoSeleccionado.collectAsState()

    if (productoSeleccionado == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No se ha encontrado el producto indicado", color = Color.Gray)
        }
        return
    }

    val producto = productoSeleccionado!!

    var cantidad by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = producto.nombreProducto,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E342E)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Volver",
                            tint = Color(0xFF4E342E)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                )
            )
        },

        bottomBar = {
            val stockDisponible = producto.stock
            var scale by remember { mutableStateOf(1f) }

            Button(
                onClick = {
                    vibrar()

                    // animación botón
                    scale = 0.9f
                    scope.launch {
                        kotlinx.coroutines.delay(100)
                        scale = 1f
                    }

                    // agregar al carrito
                    repeat(cantidad) {
                        carritoViewModel.agregarAlCarrito(producto)
                    }

                    // recargar el mismo producto desde backend
                    producto.id?.let { viewModel.seleccionarProducto(it) }

                    // mensaje
                    scope.launch {
                        snackbarHostState.showSnackbar("Agregado al carrito 🛒")
                    }
                },
                enabled = stockDisponible > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (stockDisponible > 0) Color(0xFFE57373) else Color.LightGray,
                    contentColor = Color.White
                )
            ) {
                Text(if (stockDisponible > 0) "🛒 Agregar al carrito" else "Sin stock")
            }
        }
    ) { paddingValues ->

        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // IMAGEN URL COIL
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombreProducto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = producto.nombreProducto,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF4E342E)
            )

            Text(
                text = "$${producto.precio.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF6B3143))
            )

            Text(
                text = "Stock disponible: ${producto.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = if (producto.stock > 0) Color.Gray else Color.Red
            )

            Text(
                text = producto.descripcionLarga,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            // seleccionar cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (cantidad > 1) cantidad-- },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC1CC))
                ) { Text("-", color = Color(0xFF4E342E)) }

                Text(
                    cantidad.toString(),
                    Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = { if (cantidad < producto.stock) cantidad++ },
                    enabled = cantidad < producto.stock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (cantidad < producto.stock) Color(0xFFFFC1CC) else Color.LightGray
                    )
                ) { Text("+", color = Color(0xFF4E342E)) }
            }

            Spacer(Modifier.height(60.dp))
        }
    }
}
