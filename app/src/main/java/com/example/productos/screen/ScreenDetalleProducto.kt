
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

    var cantidad by remember { mutableStateOf(1) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = productoSeleccionado!!.nombre,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4E342E)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Volver",
                            tint = Color(0xFF4E342E)
                        )
                    }
                }
            )
        },

        bottomBar = {
            val stockDisponible = productoSeleccionado!!.stock // producto seleccionado
            val scope = rememberCoroutineScope()
            var scale by remember { mutableStateOf(1f) } // PARA ANIMACIÓN DE REBOTE DEL BOTÓN

            Button(
                onClick = {

                    // VIBRACIÓN Y ANIMACIÓN DE REBOTE
                    vibrarSuave(context)
                    scale = 0.9f // achica el botón

                    // después del tiempo vuelve a ser normal
                    scope.launch {
                        kotlinx.coroutines.delay(100)
                        scale = 1f
                    }

                    // agrega productos al carro
                    repeat(cantidad) {
                        carritoViewModel.agregarAlCarrito(productoSeleccionado!!)
                    }

                    // refresca stock
                    val actualizado = viewModel.obtenerProductoPorId(productoSeleccionado!!.id)
                    viewModel.seleccionarProducto(actualizado?.id ?: productoSeleccionado!!.id)
                    // 🎉 snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar("Agregado al carrito 🛒")
                    }
                    println("✅ Agregado al carrito: ${productoSeleccionado!!.nombre} x$cantidad")
                },

                // boton
                enabled = stockDisponible > 0, // si no hay más stock se frena
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp)
                    .graphicsLayer( // aplica efecto del botón
                        scaleX = scale,
                        scaleY = scale
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (stockDisponible > 0)
                        Color(0xFFE57373) // color normal si hay stock
                    else
                        Color.LightGray // gris si no hay
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = if (stockDisponible > 0)
                        "🛒 Agregar al carrito"
                    else
                        "Sin stock",
                    color = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = productoSeleccionado!!.imagen),
                contentDescription = productoSeleccionado!!.nombre,
                modifier = Modifier
                    .height(315.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = productoSeleccionado!!.nombre,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF4E342E)
            )

            Text(
                text = "$${productoSeleccionado!!.precio}",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF6B3143))
            )

            // muestra el stock actualizado
            Text(
                text = "Stock disponible: ${productoSeleccionado!!.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = if (productoSeleccionado!!.stock > 0) Color.Gray else Color.Red
            )

            Text(
                text = productoSeleccionado!!.descripcionLarga,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (cantidad > 1) cantidad-- },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC1CC))
                ) {
                    Text("-", color = Color(0xFF4E342E))
                }

                Text(
                    text = cantidad.toString(),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                // desactiva el boton si ya no hay stock
                Button(
                    onClick = {
                        if (cantidad < productoSeleccionado!!.stock) cantidad++
                    },
                    enabled = cantidad < productoSeleccionado!!.stock,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (cantidad < productoSeleccionado!!.stock)
                            Color(0xFFFFC1CC)
                        else
                            Color.LightGray
                    )
                ) {
                    Text("+", color = Color(0xFF4E342E))
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}





















