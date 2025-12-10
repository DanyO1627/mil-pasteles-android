package com.example.productos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.productos.R
import com.example.productos.ui.theme.CafeTexto
import com.example.productos.ui.theme.RosaBoton
import com.example.productos.ui.theme.RosaFondo
import com.example.productos.viewmodel.CompraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenBoleta(
    navController: NavController,
    compraViewModel: CompraViewModel
) {
    val boleta by compraViewModel.boletaActual.collectAsState()

    // Si no hay boleta
    if (boleta == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RosaFondo),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("No hay boleta generada")
            }
        }
        return
    }

    val data = boleta!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BOLETA",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                )
            )
        },
        containerColor = RosaFondo
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(RosaFondo),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // ===== ENCABEZADO =====
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "BOLETA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = CafeTexto
                            )
                        }

                        Divider()

                        // ===== DATOS DEL CLIENTE =====
                        Text("Datos del Cliente", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RosaBoton)

                        Text("Nombre: ${data.nombre}", fontSize = 14.sp)
                        Text("Dirección: ${data.direccion}", fontSize = 14.sp)
                        Text("Comuna: ${data.comuna}", fontSize = 14.sp)
                        Text("Región: ${data.region}", fontSize = 14.sp)

                        if (data.referencia != null) {
                            Text("Referencia: ${data.referencia}", fontSize = 14.sp)
                        }

                        Text("Fecha: ${data.fecha}", fontSize = 14.sp)
                        Text(
                            "N° Boleta: ${data.numeroBoleta.toString().padStart(6, '0')}",
                            fontSize = 14.sp
                        )

                        Divider()

                        // ===== DETALLE =====
                        Text("Detalle del Pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RosaBoton)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Producto", fontWeight = FontWeight.SemiBold)
                            Text("Cant.", fontWeight = FontWeight.SemiBold)
                            Text("Total", fontWeight = FontWeight.SemiBold)
                        }

                        data.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.nombre)
                                Text("x${item.cantidad}")
                                Text(formatearPrecio(item.precio * item.cantidad))
                            }
                        }

                        Divider()

                        // ===== PAGOS =====
                        Text("Pagos", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RosaBoton)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sub Total:")
                            Text(formatearPrecio(data.subtotal))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Envío:")
                            Text(formatearPrecio(data.envio))
                        }

                        Divider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontWeight = FontWeight.Bold)
                            Text(formatearPrecio(data.total), fontWeight = FontWeight.Bold)
                        }

                        Divider()

                        Text("Gracias por su Compra", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = RosaBoton)

                        Text(
                            "Tu pedido está siendo preparado.\n" +
                                    "Recibirás un correo con el número de seguimiento.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

private fun formatearPrecio(monto: Int): String {
    val conPuntos = "%,d".format(monto).replace(',', '.')
    return "$$conPuntos"
}
