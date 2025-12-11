package com.example.productos.screen.eve

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import com.example.productos.R
import com.example.productos.model.Producto
import com.example.productos.ui.theme.*
import com.example.productos.viewmodel.CarritoViewModel
import androidx.compose.ui.text.style.TextOverflow
import com.example.productos.ui.utils.formatearPrecio
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

@Composable
fun PantallaTienda(
    navController: NavHostController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    // 👉 ESTE SÍ EXISTE EN TU VIEWMODEL
    val listaProductos by viewModel.productos.collectAsState()

    // Destacados (IDs Long)
    val destacadosIds = listOf(15L, 13L, 1L, 24L, 17L, 16L, 2L, 27L, 3L, 34L)
    val productosDestacados = listaProductos.filter { (it.id ?: 0L) in destacadosIds }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun agregarConStock(id: Int) {
        val p = viewModel.obtenerProductoPorId(id) ?: return
        carritoViewModel.agregarAlCarrito(p.id.toLong(),1)
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
                Encabezado(navController)
            }

            // CARD PRINCIPAL
            item {
                CardPrincipal(navController)
            }

            // TÍTULO
            item {
                Text(
                    text = "Nuestros productos destacados",
                    color = CafeTexto,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }

            // GRID 2 COLUMNAS
            items(productosDestacados.chunked(2).size) { fila ->
                val row = productosDestacados.chunked(2)[fila]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { producto ->
                        ProductoItemHome(
                            producto = producto,
                            navController = navController,
                            viewModel = viewModel,
                            carritoViewModel = carritoViewModel,
                            onAgregado = { agregarConStock(producto.id ?: 0L) }
                        )
                    }

                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}


@Composable
fun Encabezado(navController: NavHostController) {
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
                    contentDescription = "Carrito",
                    tint = CafeTexto,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Registro / Login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registro",
                color = CafeTexto,
                modifier = Modifier.clickable { navController.navigate("registroUsuario") }
            )
            Text(" / ", color = CafeTexto)
            Text(
                text = "Iniciar sesión",
                color = CafeTexto,
                modifier = Modifier.clickable { navController.navigate("login") }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Contáctanos",
                color = CafeTexto,
                modifier = Modifier.clickable { navController.navigate("contacto") }
            )
        }
    }
}


@Composable
fun CardPrincipal(navController: NavHostController) {
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
                Text("TIENDA ONLINE", fontWeight = FontWeight.Bold, color = CafeTexto)

                Text(
                    text = "Envíos rápidos y productos preparados con cariño.",
                    color = CafeTexto
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

@Composable
fun ProductoItemHome(
    producto: Producto,
    navController: NavHostController,
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

            // 👉 IMAGEN CON COIL (URL REAL)
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombreProducto,
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

            Text(
                text = producto.nombreProducto,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF592D2D)
            )

            Text(
                text = formatearPrecio(producto.precio),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF592D2D)
            )

            Text(
                text = "Stock: ${producto.stock}",
                color = if (producto.stock > 0) Color.Gray else Color.Red,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Button(
                onClick = onAgregado,
                enabled = producto.stock > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (producto.stock > 0) "Agregar 🛒" else "Sin stock")
            }
        }
    }
}
