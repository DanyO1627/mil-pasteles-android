package com.example.productos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Categoria(
    val nombre: String,
    val icono: ImageVector,
    val colorFondo: Color,
    val ruta: String // para navegar a los productos filtrados
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCategorias(navController: NavController) {

    // 5 categorías principales
    val categorias = listOf(
        Categoria("Tortas", Icons.Default.Cake, Color(0xFFFFC1CC), "productos/tortas"),
        Categoria("Pies y Tartas", Icons.Default.LocalDining, Color(0xFFFFE0B2), "productos/pies"),
        Categoria("Cupcakes y Mini Delicias", Icons.Default.Cookie, Color(0xFFE1BEE7), "productos/cupcakes"),
        Categoria("Postres Fríos", Icons.Default.Icecream, Color(0xFFB3E5FC), "productos/postres"),
        Categoria("Sin Gluten", Icons.Default.NoMeals, Color(0xFFC8E6C9), "productos/singluten")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías", color = Color(0xFF4E342E)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp)
        ) {
//            Text(
//                text = "Nuestras categorías",
//                style = MaterialTheme.typography.titleMedium,
//                color = Color(0xFF4E342E),
//                modifier = Modifier.padding(bottom = 12.dp)
//            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categorias) { categoria ->
                    CategoriaCard(categoria) {
                        navController.navigate("productos/${categoria.nombre}")

                    }
                }
            }
        }
    }
}

@Composable
fun CategoriaCard(categoria: Categoria, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = categoria.colorFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = categoria.icono,
                contentDescription = categoria.nombre,
                tint = Color(0xFF4E342E),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = categoria.nombre,
                color = Color(0xFF4E342E),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}