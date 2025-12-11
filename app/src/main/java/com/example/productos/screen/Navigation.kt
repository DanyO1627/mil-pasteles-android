package com.example.productos.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.example.productos.model.Producto AHORA USO DATA
import com.example.productos.viewmodel.ProductoViewModel
import com.example.productos.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.example.productos.viewmodel.CarritoViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.example.productos.screen.eve.BienvenidaScreen
import com.example.productos.screen.eve.LoginScreen
import com.example.productos.screen.eve.PantallaTienda
import com.example.productos.viewmodel.UsuarioViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productos.viewmodel.CompraViewModel

// aquí vive toddo lo relacionado con la navegacion (navcontroller, navhost, rutas, appnavhost,etc)
// y así aliviamos nuestras otras pantallas

enum class Destination (
    val route:String,
    val label: String,
    val contentDescription: String,
    val iconVector: androidx.compose.ui.graphics.vector.ImageVector?=null,
    val iconRes:Int?=null
){
    // esto es como el catalogo fijo de cada lado dónde puedo navegar
    HOME("home", "Home", "Ir a home", iconVector = Icons.Default.Home),
    PRODUCTOS("productos", "Productos", "Ir a productos", iconRes = R.drawable.vector_pastel),
    CATEGORIAS("categorias", "Categorías", "Ir a categorías", iconVector = Icons.Default.Category),
    CARRITO("carrito", "Carrito", "Ir al carrito", iconVector = Icons.Default.ShoppingCart),
    PERFIL("perfil", "Nosotros", "Ir a Nosotros", iconVector = Icons.Default.Groups)
}


// BARRA INFERIORR Y NAVHOST

@Composable
fun NavigationBarMain(
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    modifier: Modifier = Modifier

) {
    val navController = rememberNavController()
    var selectedDestination by rememberSaveable { mutableStateOf(Destination.PRODUCTOS) }

    val usuarioViewModel: UsuarioViewModel = viewModel()
    val compraViewModel: CompraViewModel = viewModel()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                Destination.values().forEach { destination ->
                    NavigationBarItem(
                        selected = selectedDestination == destination,
                        onClick = {
                            selectedDestination = destination
                            navController.navigate(destination.route) {
                                popUpTo(Destination.HOME.route) // esto es para que la pantalla en la que se inicia
                                launchSingleTop = true              // sea home
                            }
                        },
                        icon = {
                            destination.iconVector?.let {
                                Icon(imageVector = it, contentDescription = destination.contentDescription)
                            } ?: destination.iconRes?.let {
                                Icon(painter = painterResource(id = it), contentDescription = destination.contentDescription)
                            }
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            viewModel = viewModel,
            carritoViewModel = carritoViewModel,
            usuarioViewModel = usuarioViewModel,
            compraViewModel = compraViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


// MAPA DE RUTAS
// (con animaciones entre cambios de pantalla)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    usuarioViewModel : UsuarioViewModel,
    compraViewModel: CompraViewModel,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "bienvenida",
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {



        composable("bienvenida") {
            BienvenidaScreen(navController)
        }

        composable("login") {
            LoginScreen(navController,usuarioViewModel)
        }


        composable(Destination.HOME.route) {
            PantallaTienda(
                navController = navController,
                viewModel = viewModel,
                carritoViewModel = carritoViewModel
            )
        }

        // dentro de AppNavHost, junto con las otras composable(...)
        composable("registroUsuario") {
            ScreenRegistroUsuario(navController = navController,usuarioViewModel)
        }


        composable("contacto") {
            ScreenContacto(navController)
        }

        composable(Destination.PRODUCTOS.route) {
            ScreenProductos(navController, viewModel, carritoViewModel)
        }

        // categorias
        composable(Destination.CATEGORIAS.route) {
            ScreenCategorias(navController)
        }

        composable(Destination.CARRITO.route) {
            ScreenCarrito(navController, carritoViewModel, viewModel)
        }

        // sobre nosotros
        composable(Destination.PERFIL.route) {
            ScreenNosotros(navController)
        }

        // detalle de cada producto
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getLong("id")

            if (id != null) {
                viewModel.seleccionarProducto(id)
                ScreenDetalleProducto(
                    navController = navController,
                    viewModel = viewModel,
                    carritoViewModel = carritoViewModel
                )
            } else {
                navController.popBackStack()
            }
        }

        composable("compraExitosa") {
            ScreenCompra(
                navController = navController,
                carritoViewModel = carritoViewModel,
                compraViewModel = compraViewModel
            )
        }

        composable("boleta") {
            ScreenBoleta(
                navController = navController,
                compraViewModel = compraViewModel
            )
        }


        // productos filtrados por categoria
        composable(
            route = "productos/{categoriaNombre}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val categoriaNombre = backStackEntry.arguments?.getString("categoriaNombre") ?: ""
            ScreenProductosCategoria(
                navController = navController,
                viewModel = viewModel,
                carritoViewModel = carritoViewModel,
                categoriaNombre = categoriaNombre
            )
        }
    }
}

