package com.example.productos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.productos.R
import com.example.productos.data.Producto
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch


// LA TOPBAR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProductos() {
    // SEGÚN YO EN ESTOS MOEMNTOS NO ESTÁ OPERATIVO EL MENU
    var menuAbierto by remember { mutableStateOf(false) }

    // top bar caja
    TopAppBar(
        title = {
            Text(
                "Nuestros productos",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF4E342E) // Café Oscuro (Brown 800) -  el color del texto
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFFFFA6B8) // ESTE ES EL ROSA PASTEL
        ),
        // SEGÚN YO EN ESTOS MOEMNTOS NO ESTÁ OPERATIVO EL MENU
        navigationIcon = {
            IconButton(onClick = { menuAbierto = !menuAbierto }) {
                Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color(0xFF4E342E))
            }
        },
        actions = {
            IconButton(onClick = { /* ir a inicio si quieres */ }) { // estaba vacío, dejé un comentario de la acción
                Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF4E342E))
            }
        }
    )
}

// EL BUSCADOR
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp) // tamaño del buscador
    )
}

// LA LISTA DE PRODUCTOS
@Composable
fun ScreenProductos(
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val productos by viewModel.listaProductos.collectAsState()

    // guarda lo que escribe el usuario en el buscador
    var query by remember { mutableStateOf("") }

    // luego filtra por eso
    val productosFiltrados =
        if (query.isBlank()) productos else productos.filter { it.nombre.contains(query, true) } // usé 'true' en vez de 'ignoreCase = true' que es lo mismo

    // snackbar + scope
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBarProductos() },
        snackbarHost = { SnackbarHost(snackbarHostState) } //  host para snackbars, para mostrar los mensajes
    ) { paddingValues ->
        //  Column   para que toddo salga ordenado vertical
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White) // color del fondo
                .padding(16.dp)
        ) {
            // buscador arriba de los productos
            BuscadorProductos(query) { nuevoTexto -> query = nuevoTexto }

            // lista de productos filtrada
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp // espacio extra para que los botones no se corten
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize() // importante, no fillMaxHeight
                    .padding(bottom = 8.dp)
            ) {
                items(productosFiltrados) { producto ->
                    ProductoItem(
                        producto = producto,
                        navController = navController,
                        viewModel = viewModel,
                        carritoViewModel = carritoViewModel, // para el carrito
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

// LA TARJETA DE CADA PRODUCTO
@Composable
fun ProductoItem(
    producto: Producto,
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    onAgregado: () -> Unit // llama al mensaje (snackbar)
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(340.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC1CC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //  SOLO la imagen será clickeable ahora
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { // click de detalle
                        viewModel.seleccionarProducto(producto.id)
                        navController.navigate("detalle/${producto.id}")
                    },
                contentScale = ContentScale.Crop
            )
            // esto recorta la img para que rellene el espacio

            // fondo de la tarjeta de cada producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) // esto le da un recuadro a la columna que contenía el nombre y el precio, pero feo
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // esto para que si el nombre es muy largo la tarjeta se acomode
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = viewModel.formatearPrecio(producto.precio),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF592D2D)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Row sirve como mini contenedor que te pone ttodo uno al lado del otro (cambiado a Column)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Mostrar stock actual
                    Text(
                        text = "Stock: ${producto.stock}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (producto.stock > 0) Color.Gray else Color.Red
                    )

                    // Botón: Agregar al carrito
                    Button(
                        onClick = {
                            carritoViewModel.agregarAlCarrito(producto)
                            onAgregado()
                        },
                        enabled = producto.stock > 0, // 🔹 desactiva si no hay stock
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (producto.stock > 0) Color(0xFF592D2D) else Color.LightGray,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(if (producto.stock > 0) "Agregar 🛒" else "Sin stock")
                    }

                    // Botón: Detalles
                    Button(
                        onClick = {
                            viewModel.seleccionarProducto(producto.id)
                            navController.navigate("detalle/${producto.id}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF8A9E),
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text("Detalles")
                    }
                }


                Spacer(modifier = Modifier.height(8.dp)) //  espacio para que no se corte el boton
            }
        }
    }
}