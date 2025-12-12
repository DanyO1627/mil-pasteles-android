package com.example.productos.screen.eve

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.productos.R
import com.example.productos.data.Producto
import com.example.productos.ui.theme.*
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

@Composable
fun PantallaTienda(
    navController: NavHostController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val lista by viewModel.listaProductos.collectAsState()
    val productos = lista.filter { it.id in listOf(15, 13, 1, 24, 17, 16, 2, 27, 3, 34) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun agregarConStock(id: Int) {
        val p = viewModel.obtenerProductoPorId(id) ?: return
        carritoViewModel.agregarAlCarrito(p)
        scope.launch {
            snackbarHostState.showSnackbar("Agregado al carrito 🛒")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(RosaFondo)
                .padding(paddingValues)
        ) {
            // HEADER
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pastelería Mil Sabores",
                            color = CafeTexto,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = { navController.navigate("carrito") },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.carrito),
                                contentDescription = "Carrito de compras",
                                tint = CafeTexto,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Registro",
                            color = CafeTexto,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate("registroUsuario")
                            }
                        )

                        Text(
                            text = " / ",
                            color = CafeTexto,
                            fontSize = 14.sp
                        )

                        Text(
                            text = "Iniciar sesión",
                            color = CafeTexto,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate("login")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contactanosssss",
                            color = CafeTexto,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navController.navigate("contacto")
                            }
                        )
                    }
                }
            }

            // CARD PRINCIPAL
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.cake),
                            contentDescription = "Tienda Online",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "TIENDA ONLINE",
                                fontWeight = FontWeight.Bold,
                                color = CafeTexto,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "Envíos rápidos y productos preparados con cariño.",
                                color = CafeTexto,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Button(
                                onClick = { navController.navigate("productos") },
                                colors = ButtonDefaults.buttonColors(containerColor = RosaBoton),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Ver productos", color = Color.White)
                            }
                        }
                    }
                }
            }

            // TÍTULO PRODUCTOS
            item {
                Text(
                    text = "Nuestros productos destacados",
                    color = CafeTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }

            // PRODUCTOS (dos columnas)
            items(productos.chunked(2).size) { rowIndex ->
                val rowItems = productos.chunked(2)[rowIndex]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { producto ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductoItemHome(
                                producto = producto,
                                navController = navController,
                                viewModel = viewModel,
                                carritoViewModel = carritoViewModel,
                                onAgregado = { id -> agregarConStock(id) }
                            )
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun ProductoItemHome(
    producto: Producto,
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    onAgregado: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC1CC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        viewModel.seleccionarProducto(producto.id)
                        navController.navigate("detalle/${producto.id}")
                    },
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF592D2D)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = viewModel.formatearPrecio(producto.precio),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF592D2D)
                )

                Text(
                    text = "Stock: ${producto.stock}",
                    fontSize = 12.sp,
                    color = if (producto.stock > 0) Color.Gray else Color.Red,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onAgregado(producto.id) },
                        enabled = producto.stock > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (producto.stock > 0) Color(0xFF592D2D) else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (producto.stock > 0) "Agregar 🛒" else "Sin stock")
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.seleccionarProducto(producto.id)
                            navController.navigate("detalle/${producto.id}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF592D2D)
                        )
                    ) {
                        Text("Ver detalles", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}