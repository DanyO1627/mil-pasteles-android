package com.example.productos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.ProductoViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch
import com.example.productos.data.Producto
import androidx.compose.ui.text.font.FontWeight
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProductosCategoria(
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    categoriaNombre: String
) {
    val lista by viewModel.listaProductos.collectAsState()
    val productos = lista.filter { it.categoriaId == categoriaNombreToId(categoriaNombre) }


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Wrapper como en ScreenHome
    fun agregarConStock(id: Int) {
        val p = viewModel.obtenerProductoPorId(id) ?: return

        carritoViewModel.agregarAlCarrito(p.id.toLong(),1)

        scope.launch {
            snackbarHostState.showSnackbar("Agregado al carrito 🛒")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(categoriaNombre, color = Color(0xFF4E342E)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFFFA6B8)),
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
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            if (productos.isEmpty()) {
                Text("No hay productos en esta categoría aún 🍰", color = Color.Gray)
            } else {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(productos) { producto ->
                        ProductoItemFiltrado(
                            producto = producto,
                            navController = navController,
                            viewModel = viewModel,
                            carritoViewModel = carritoViewModel,
                            onAgregado = { id -> agregarConStock(id) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ProductoItemFiltrado(
    producto: Producto,
    navController: NavController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    onAgregado: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC1CC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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
                    .clickable {
                        viewModel.seleccionarProducto(producto.id)
                        navController.navigate("detalle/${producto.id}")
                    },
                contentScale = ContentScale.Crop
            )

            Column(Modifier.fillMaxWidth()) {

                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    text = viewModel.formatearPrecio(producto.precio),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Stock: ${producto.stock}",
                    color = if (producto.stock > 0) Color.Gray else Color.Red
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onAgregado(producto.id) },
                    enabled = producto.stock > 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6B8)),
                ) {
                    Text(if (producto.stock > 0) "Agregar 🛒" else "Sin stock")
                }
            }
        }
    }
}
fun categoriaNombreToId(nombre: String): Int {
    return when (nombre) {
        "Tortas" -> 1
        "Pies y Tartas" -> 2
        "Cupcakes y Mini Delicias" -> 3
        "Postres Fríos" -> 4
        "Sin Gluten" -> 5
        else -> -1
    }
}
