package com.example.productos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.productos.ui.utils.formatearPrecio
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.productos.R
import com.example.productos.data.CarritoItem
import com.example.productos.viewmodel.CarritoViewModel
import com.example.productos.viewmodel.CompraViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCompra(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    compraViewModel: CompraViewModel
) {
    val lista by carritoViewModel.listaCarrito.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Datos del formulario
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    // Dropdowns
    var expandedRegion by remember { mutableStateOf(false) }
    var expandedComuna by remember { mutableStateOf(false) }

    // Métodos de pago
    val metodosPago = listOf("Débito", "Crédito", "Webpay")
    var metodoPagoSeleccionado by remember { mutableStateOf(metodosPago.first()) }

    // Lista de regiones (simple)
    val regiones = listOf(
        "Región Metropolitana", "Valparaíso", "Biobío",
        "Coquimbo", "Los Lagos", "Maule", "O'Higgins"
    )

    // Comunas por región
    val comunasPorRegion = mapOf(
        "Región Metropolitana" to listOf("Santiago", "Providencia", "Maipú", "Ñuñoa"),
        "Valparaíso" to listOf("Valparaíso", "Viña del Mar", "Concón"),
        "Biobío" to listOf("Concepción", "Talcahuano", "Chiguayante")
    )

    // Cálculo de montos
    val subtotal = lista.sumOf { (it.precio * it.cantidad).toInt() }
    val envio = when (region) {
        "" -> 0
        "Región Metropolitana" -> 3990
        else -> 5990
    }
    val total = subtotal + envio

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compra", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFA6B8))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFFFF3F6)
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFFF3F6))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {

            // DATOS DEL ENVÍO
            item {
                DatosEnvioCard(
                    nombre, { nombre = it },
                    direccion, { direccion = it },
                    region, { region = it; comuna = "" },
                    comuna, { comuna = it },
                    referencia, { referencia = it },
                    regiones, comunasPorRegion,
                    expandedRegion, expandedComuna,
                    { expandedRegion = it }, { expandedComuna = it }
                )
            }

            // TU PEDIDO
            item {
                TuPedidoCard(lista)
            }

            // MÉTODO DE PAGO
            item {
                MetodoPagoCard(
                    metodos = metodosPago,
                    seleccionado = metodoPagoSeleccionado,
                    onChange = { metodoPagoSeleccionado = it }
                )
            }

            // RESUMEN FINAL
            item {
                ResumenCard(
                    subtotal = subtotal,
                    envio = envio,
                    total = total,
                    onSeguir = { navController.popBackStack() },
                    onPagar = {
                        when {
                            lista.isEmpty() ->
                                scope.launch { snackbarHostState.showSnackbar("Tu carrito está vacío 🛒") }

                            nombre.isBlank() || direccion.isBlank() ||
                                    region.isBlank() || comuna.isBlank() ->
                                scope.launch { snackbarHostState.showSnackbar("Completa los campos ✏️") }

                            else -> {
                                compraViewModel.generarBoleta(
                                    nombre = nombre,
                                    direccion = direccion,
                                    region = region,
                                    comuna = comuna,
                                    referencia = referencia.ifBlank { null },
                                    metodoPago = metodoPagoSeleccionado,
                                    items = lista,
                                    envio = envio
                                )

                                carritoViewModel.confirmarCompra()

                                navController.navigate("boleta")
                            }
                        }
                    }
                )
            }
        }
    }
}

////////////////////////////////////////
/// COMPONENTES REPARADOS
////////////////////////////////////////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEnvioCard(
    nombre: String, onNombre: (String) -> Unit,
    direccion: String, onDireccion: (String) -> Unit,
    region: String, onRegion: (String) -> Unit,
    comuna: String, onComuna: (String) -> Unit,
    referencia: String, onReferencia: (String) -> Unit,
    regiones: List<String>,
    comunasPorRegion: Map<String, List<String>>,
    expandedRegion: Boolean,
    expandedComuna: Boolean,
    onExpandRegion: (Boolean) -> Unit,
    onExpandComuna: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Datos del envío", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            OutlinedTextField(
                value = nombre,
                onValueChange = onNombre,
                label = { Text("Nombre completo *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = onDireccion,
                label = { Text("Dirección *") },
                modifier = Modifier.fillMaxWidth()
            )

            // REGION
            ExposedDropdownMenuBox(
                expanded = expandedRegion,
                onExpandedChange = { onExpandRegion(!expandedRegion) }
            ) {
                OutlinedTextField(
                    value = region,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Región *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedRegion) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedRegion,
                    onDismissRequest = { onExpandRegion(false) }
                ) {
                    regiones.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                onRegion(it)
                                onExpandRegion(false)
                            }
                        )
                    }
                }
            }

            // COMUNA
            ExposedDropdownMenuBox(
                expanded = expandedComuna,
                onExpandedChange = {
                    if (region.isNotBlank()) onExpandComuna(!expandedComuna)
                }
            ) {
                OutlinedTextField(
                    value = comuna,
                    onValueChange = {},
                    readOnly = true,
                    enabled = region.isNotEmpty(),
                    label = { Text("Comuna *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedComuna) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedComuna,
                    onDismissRequest = { onExpandComuna(false) }
                ) {
                    (comunasPorRegion[region] ?: emptyList()).forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                onComuna(it)
                                onExpandComuna(false)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = referencia,
                onValueChange = onReferencia,
                label = { Text("Referencia (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

////////////////////////////////////////
// TU PEDIDO
////////////////////////////////////////

@Composable
fun TuPedidoCard(lista: List<CarritoItem>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text("Tu pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            if (lista.isEmpty()) {
                Text("Tu carrito está vacío", color = Color.Gray)
            } else {
                lista.forEach { PedidoItemRow(it) }
            }
        }
    }
}

@Composable
private fun PedidoItemRow(item: CarritoItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.nombre,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(10.dp))

        Column(Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("x${item.cantidad}", fontSize = 12.sp, color = Color.Gray)
        }

        Text(formatearPrecio(item.precio * item.cantidad))
    }
}

////////////////////////////////////////
// MÉTODO DE PAGO
////////////////////////////////////////

@Composable
fun MetodoPagoCard(
    metodos: List<String>,
    seleccionado: String,
    onChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Método de pago", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                metodos.forEach { metodo ->
                    Button(
                        onClick = { onChange(metodo) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado == metodo)
                                Color(0xFFFF8A9E) else Color(0xFFFFE1E7),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(metodo, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

////////////////////////////////////////
// RESUMEN CARD
////////////////////////////////////////

@Composable
fun ResumenCard(
    subtotal: Int,
    envio: Int,
    total: Int,
    onSeguir: () -> Unit,
    onPagar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Sub total:")
                Text(formatearPrecio(subtotal))
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Envío:")
                Text(if (envio > 0) formatearPrecio(envio) else "$0")
            }

            Divider()

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total:", fontWeight = FontWeight.Bold)
                Text(formatearPrecio(total), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                OutlinedButton(
                    onClick = onSeguir,
                    modifier = Modifier.weight(1f)
                ) { Text("Seguir buscando") }

                Button(
                    onClick = onPagar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A9E))
                ) { Text("Ir a pagar", color = Color.White) }
            }
        }
    }
}


