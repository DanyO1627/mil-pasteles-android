package com.example.productos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.example.productos.ui.utils.formatearPrecio
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch


// TOPBAR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProductos() {
    TopAppBar(
        title = {
            Text(
                "Nuestros productos",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF4E342E)
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color(0xFF4E342E))
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF4E342E))
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFFFFA6B8)
        )
    )
}

// BUSCADOR
@Composable
fun BuscadorProductos(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar producto...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )
}


// LISTA PRINCIPAL
@Composable
fun ScreenProductos(
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    // carga los productos del backend a penas se entra a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }
    val productos by viewModel.productos.collectAsState()

    var query by remember { mutableStateOf("") }

    val productosFiltrados =
        if (query.isBlank()) productos
        else productos.filter { it.nombreProducto.contains(query, ignoreCase = true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBarProductos() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            BuscadorProductos(query) { query = it }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = productosFiltrados,
                    key = { it.id!! }
                ) { producto ->
                    ProductoItem(
                        producto = producto,
                        navController = navController,
                        viewModel = viewModel,
                        carritoViewModel = carritoViewModel,
                        onAgregado = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Agregado al carrito 🛒")
                            }
                        }
                    )
                }
            }
        }
    }
}


// TARJETA DE PRODUCTO
@Composable
fun ProductoItem(
    producto: com.example.productos.model.Producto,
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    onAgregado: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC1CC)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // IMAGEN DESDE URL (COIL)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(producto.imagenUrl)
                    .crossfade(true)
                    .size(300) //
                    .build(),
                contentDescription = producto.nombreProducto,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        viewModel.seleccionarProducto(producto.id)
                        navController.navigate("detalle/${producto.id}")
                    }
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = producto.nombreProducto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatearPrecio(producto.precio),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF592D2D)
                )

                Spacer(Modifier.height(4.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(
                        text = "Stock: ${producto.stock}",
                        color = if (producto.stock > 0) Color.Gray else Color.Red
                    )

                    Button(
                        onClick = {
                            carritoViewModel.agregarAlCarrito(producto)
                            onAgregado()
                        },
                        enabled = producto.stock > 0,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (producto.stock > 0) Color(0xFF592D2D) else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (producto.stock > 0) "Agregar 🛒" else "Sin stock")
                    }

                    Button(
                        onClick = {
                            viewModel.seleccionarProducto(producto.id)
                            navController.navigate("detalle/${producto.id}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF8A9E),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Detalles")
                    }
                }
            }
        }
    }
}
