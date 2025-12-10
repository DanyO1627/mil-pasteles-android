package com.example.productos.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.productos.data.CarritoItem
import com.example.productos.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch
import com.example.productos.ui.theme.*
import com.example.productos.viewmodel.ProductoViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.example.productos.ui.utils.vibrarFuerte
import com.example.productos.ui.utils.vibrarSuave

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCarrito(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    productoViewModel: ProductoViewModel
) {
    val lista by carritoViewModel.listaCarrito.collectAsState()
    val total by carritoViewModel.total.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    fun triggerHapticFeedbackSuave() {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        vibrarSuave(context)
    }

    fun triggerHapticFeedbackFuerte() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        vibrarFuerte(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontWeight = FontWeight.Bold) },
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
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Total: ${productoViewModel.formatearPrecio(total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            triggerHapticFeedbackFuerte()
                            navController.navigate("compraExitosa")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF592D2D),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ir a pagar", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    ) { innerPadding ->

        // ======== CONTENEDOR SCROLEABLE (LAZY COLUMN) ========
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFF8F9)),
            contentPadding = PaddingValues(bottom = 130.dp)
        ) {

            if (lista.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(top = 150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                "🛒 Tu carrito está vacío",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { navController.navigate("productos") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFA6B8),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Ver productos")
                            }
                        }
                    }
                }
            } else {

                // Lista de productos
                items(lista, key = { it.id }) { item ->
                    CarritoItemCard(
                        item = item,
                        carritoViewModel = carritoViewModel,
                        productoViewModel = productoViewModel
                    )
                }

                // Botón vaciar carrito
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            carritoViewModel.vaciarCarrito()
                            scope.launch { snackbarHostState.showSnackbar("Carrito vaciado") }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC1CC),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Vaciar carrito")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
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
    var offsetX by remember { mutableStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX < -150f) carritoViewModel.disminuirCantidad(item.copy(cantidad = 1))
                        offsetX = 0f
                    },
                    onDrag = { change, dragAmount ->
                        offsetX += dragAmount.x
                        change.consume()
                    }
                )
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = item.imagen),
                contentDescription = item.nombre,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(item.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Precio: ${productoViewModel.formatearPrecio(item.precio)}", fontSize = 14.sp)

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        IconButton(onClick = { carritoViewModel.disminuirCantidad(item) }) {
                            Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                        }

                        Text("${item.cantidad}", fontSize = 16.sp)

                        val productoOriginal = productoViewModel.obtenerProductoPorId(item.id)
                        val stockDisponible = productoOriginal?.stock ?: 0
                        val puedeAgregar = stockDisponible > 0

                        IconButton(
                            onClick = { carritoViewModel.aumentarCantidad(item) },
                            enabled = puedeAgregar
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Aumentar",
                                tint = if (puedeAgregar) Color.Black else Color.LightGray
                            )
                        }
                    }

                    Text(
                        "Subtotal: ${productoViewModel.formatearPrecio(item.precio * item.cantidad)}",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = { carritoViewModel.eliminar(item) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Gray
                )
            }
        }
    }
}
