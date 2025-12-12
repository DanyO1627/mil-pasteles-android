package com.example.productos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.productos.R
import com.example.productos.data.CarritoItem
import com.example.productos.ui.theme.CafeTexto
import com.example.productos.ui.theme.RosaBoton
import com.example.productos.ui.theme.RosaFondo
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    //  datos del formulario
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }

    // dropdowns
    var expandedRegion by remember { mutableStateOf(false) }
    var expandedComuna by remember { mutableStateOf(false) }

    // métodos de pago
    val metodos = listOf("Débito", "Crédito", "Webpay")
    var metodoPagoSeleccionado by remember { mutableStateOf("Débito") }

    // regiones simples
    val regiones = listOf(
        "Arica y Parinacota",
        "Tarapacá",
        "Antofagasta",
        "Atacama",
        "Coquimbo",
        "Valparaíso",
        "Región Metropolitana",
        "O'Higgins",
        "Maule",
        "Ñuble",
        "Biobío",
        "La Araucanía",
        "Los Ríos",
        "Los Lagos",
        "Aysén",
        "Magallanes"
    )

    val comunasPorRegion = mapOf(
        "Arica y Parinacota" to listOf("Arica", "Camarones", "Putre", "General Lagos"),
        "Tarapacá" to listOf("Alto Hospicio", "Iquique", "Huara", "Camiña", "Colchane", "Pica", "Pozo Almonte"),
        "Antofagasta" to listOf(
            "Antofagasta", "Mejillones", "Sierra Gorda", "Taltal",
            "Calama", "Ollagüe", "San Pedro de Atacama",
            "Tocopilla", "María Elena"
        ),
        "Atacama" to listOf(
            "Copiapó", "Caldera", "Tierra Amarilla", "Chañaral",
            "Diego de Almagro", "Vallenar", "Freirina", "Huasco", "Alto del Carmen"
        ),
        "Coquimbo" to listOf(
            "La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paihuano", "Vicuña",
            "Ovalle", "Monte Patria", "Punitaqui", "Río Hurtado",
            "Combarbalá", "Illapel", "Salamanca", "Los Vilos", "Canela"
        ),
        "Valparaíso" to listOf(
            "Valparaíso", "Viña del Mar", "Concón", "Quilpué", "Villa Alemana", "Casablanca",
            "Quintero", "Puchuncaví", "Quillota", "La Calera", "La Cruz", "Nogales",
            "Hijuelas", "Limache", "Olmué", "San Antonio", "Cartagena", "El Tabo",
            "El Quisco", "Algarrobo", "Santo Domingo", "San Felipe", "Catemu", "Llaillay",
            "Panquehue", "Putaendo", "Santa María", "Los Andes", "Calle Larga", "Rinconada",
            "San Esteban", "Isla de Pascua", "Juan Fernández", "Petorca", "La Ligua",
            "Cabildo", "Zapallar", "Papudo"
        ),
        "O'Higgins" to listOf(
            "Rancagua", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "Las Cabras",
            "Machalí", "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua",
            "Quinta de Tilcoco", "Rengo", "Requínoa", "San Vicente", "Pichilemu",
            "La Estrella", "Litueche", "Marchigüe", "Navidad", "Paredones", "San Fernando",
            "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla", "Peralillo",
            "Placilla", "Pumanque", "Santa Cruz"
        ),
        "Maule" to listOf(
            "Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco",
            "Pencahue", "Río Claro", "San Clemente", "San Rafael", "Cauquenes", "Chanco",
            "Pelluhue", "Curicó", "Hualañé", "Licantén", "Molina", "Rauco", "Romeral",
            "Sagrada Familia", "Teno", "Vichuquén", "Linares", "Colbún", "Longaví",
            "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"
        ),
        "Ñuble" to listOf(
            "Chillán", "Chillán Viejo", "Coihueco", "El Carmen", "Pinto", "San Ignacio",
            "Pemuco", "Yungay", "Quillón", "Bulnes", "San Nicolás", "San Carlos",
            "Ñiquén", "San Fabián", "Coelemu", "Ránquil", "Trehuaco", "Cobquecura",
            "Ninhue", "Quirihue", "Portezuelo"
        ),
        "Biobío" to listOf(
            "Concepción", "Coronel", "Chiguayante", "Florida", "Hualqui", "Lota",
            "Penco", "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Hualpén",
            "Cabrero", "Laja", "Los Ángeles", "Mulchén", "Nacimiento", "Negrete",
            "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel", "Yumbel",
            "Alto Biobío", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Lebu",
            "Los Álamos", "Tirúa"
        ),
        "La Araucanía" to listOf(
            "Temuco", "Carahue", "Cholchol", "Cunco", "Curarrehue", "Freire", "Galvarino",
            "Gorbea", "Lautaro", "Loncoche", "Melipeuco", "Nueva Imperial", "Padre Las Casas",
            "Perquenco", "Pitrufquén", "Pucón", "Saavedra", "Teodoro Schmidt", "Toltén",
            "Vilcún", "Villarrica", "Angol", "Collipulli", "Curacautín", "Ercilla",
            "Lonquimay", "Los Sauces", "Lumaco", "Purén", "Renaico", "Traiguén", "Victoria"
        ),
        "Los Ríos" to listOf(
            "Valdivia", "Corral", "Lanco", "Los Lagos", "Máfil", "Mariquina",
            "Paillaco", "Panguipulli", "La Unión", "Futrono", "Lago Ranco", "Río Bueno"
        ),
        "Los Lagos" to listOf(
            "Puerto Montt", "Calbuco", "Cochamó", "Maullín", "Llanquihue", "Fresia",
            "Frutillar", "Los Muermos", "Puerto Varas", "Osorno", "Puerto Octay",
            "Purranque", "Puyehue", "Río Negro", "San Juan de la Costa", "San Pablo",
            "Castro", "Ancud", "Chonchi", "Curaco de Vélez", "Dalcahue", "Puqueldón",
            "Queilén", "Quellón", "Quemchi", "Quinchao", "Chaitén", "Futaleufú",
            "Hualaihué", "Palena"
        ),
        "Aysén" to listOf(
            "Coyhaique", "Lago Verde", "Aysén", "Cisnes", "Guaitecas",
            "Cochrane", "O’Higgins", "Tortel", "Chile Chico", "Río Ibáñez"
        ),
        "Magallanes" to listOf(
            "Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio",
            "Cabo de Hornos (Puerto Williams)", "Antártica", "Porvenir", "Primavera",
            "Timaukel", "Natales", "Torres del Paine"
        ),
        "Región Metropolitana" to listOf(
            "Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque",
            "Estación Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida",
            "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo",
            "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén",
            "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca",
            "San Joaquín", "San Miguel", "San Ramón", "Vitacura", "Puente Alto", "Pirque",
            "San José de Maipo", "Colina", "Lampa", "Tiltil", "Buin", "Calera de Tango",
            "Paine", "San Bernardo", "Melipilla", "Alhué", "Curacaví", "María Pinto",
            "San Pedro", "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"
        )
    )

    // 💰 cálculo de montos
    val subtotal = lista.sumOf { it.precio * it.cantidad }
    val envio = when {
        region.isBlank() -> 0
        region == "Región Metropolitana" -> 3990
        else -> 5990
    }
    val total = subtotal + envio

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Compra", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = RosaFondo
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(RosaFondo)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {

            // ========== DATOS DEL ENVÍO ==============
            item {
                DatosEnvioCard(
                    nombre = nombre, onNombre = { nombre = it },
                    direccion = direccion, onDireccion = { direccion = it },
                    region = region, onRegion = { region = it; comuna = "" },
                    comuna = comuna, onComuna = { comuna = it },
                    referencia = referencia, onReferencia = { referencia = it },
                    regiones = regiones,
                    comunasPorRegion = comunasPorRegion,
                    expandedRegion = expandedRegion,
                    expandedComuna = expandedComuna,
                    onExpandRegion = { expandedRegion = it },
                    onExpandComuna = { expandedComuna = it }
                )
            }

            // ========== TU PEDIDO ==============
            item {
                TuPedidoCard(lista)
            }

            // ========== METODO DE PAGO ==============
            item {
                MetodoPagoCard(
                    metodos = metodos,
                    seleccionado = metodoPagoSeleccionado,
                    onChange = { metodoPagoSeleccionado = it }
                )
            }

            // ========== RESUMEN TOTAL ==============
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

                            nombre.isBlank() || direccion.isBlank()
                                    || region.isBlank() || comuna.isBlank() ->
                                scope.launch { snackbarHostState.showSnackbar("Completa los campos ✏️") }

                            else -> {
                                compraViewModel.generarBoleta(
                                    nombre = nombre,
                                    direccion = direccion,
                                    region = region,
                                    comuna = comuna,
                                    referencia = referencia.ifBlank { null },
                                    metodoPago = metodoPagoSeleccionado,
                                    items = lista.toList(),
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

//////////////////////////////////////////////////////////
// COMPONENTES
//////////////////////////////////////////////////////////
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
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text("Datos del envío", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CafeTexto)

            OutlinedTextField(
                value = nombre,
                onValueChange = onNombre,
                label = { Text("Nombre completo *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = onDireccion,
                label = { Text("Dirección *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {

                // región
                ExposedDropdownMenuBox(
                    expanded = expandedRegion,
                    onExpandedChange = { onExpandRegion(!expandedRegion) },
                    modifier = Modifier.weight(1f)
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
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                onRegion(it)
                                onExpandRegion(false)
                            })
                        }
                    }
                }

                // comuna
                ExposedDropdownMenuBox(
                    expanded = expandedComuna,
                    onExpandedChange = { if (region.isNotBlank()) onExpandComuna(!expandedComuna) },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = comuna,
                        onValueChange = {},
                        readOnly = true,
                        enabled = region.isNotBlank(),
                        label = { Text("Comuna *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedComuna) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedComuna,
                        onDismissRequest = { onExpandComuna(false) }
                    ) {
                        (comunasPorRegion[region] ?: emptyList()).forEach {
                            DropdownMenuItem(text = { Text(it) }, onClick = {
                                onComuna(it)
                                onExpandComuna(false)
                            })
                        }
                    }
                }
            }

            OutlinedTextField(
                value = referencia,
                onValueChange = onReferencia,
                label = { Text("Referencia (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun TuPedidoCard(lista: List<CarritoItem>) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text("Tu pedido", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CafeTexto)

            if (lista.isEmpty()) {
                Text("Tu carrito está vacío", color = Color.Gray)
            } else {
                lista.forEach { PedidoItemRow(it) }
            }
        }
    }
}

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
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Text("Método de pago", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CafeTexto)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                metodos.forEach { metodo ->
                    Button(
                        onClick = { onChange(metodo) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado == metodo) RosaBoton else Color(0xFFFFE0E6),
                            contentColor = if (seleccionado == metodo) Color.White else CafeTexto
                        ),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(metodo, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

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
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Sub total:", fontWeight = FontWeight.Medium)
                Text(formatearPrecio(subtotal))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Envío:", fontWeight = FontWeight.Medium)
                Text(if (envio > 0) formatearPrecio(envio) else "$0")
            }

            Divider()

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total:", fontWeight = FontWeight.Bold)
                Text(formatearPrecio(total), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {

                OutlinedButton(
                    onClick = onSeguir,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Seguir buscando")
                }

                Button(
                    onClick = onPagar,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RosaBoton)
                ) {
                    Text("Ir a pagar", color = Color.White)
                }
            }
        }
    }
}

//////////////////////////////////////////////////////////
// ITEM DE PEDIDO
//////////////////////////////////////////////////////////

@Composable
private fun PedidoItemRow(item: CarritoItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imagen),
            contentDescription = item.nombre,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text("x${item.cantidad}", fontSize = 12.sp, color = Color.Gray)
        }

        Text(formatearPrecio(item.precio * item.cantidad))
    }
}

//////////////////////////////////////////////////////////
// FORMATEO MONEDA
//////////////////////////////////////////////////////////

private fun formatearPrecio(monto: Int): String {
    if (monto <= 0) return "$0"
    val conPuntos = "%,d".format(monto).replace(',', '.')
    return "$$conPuntos"
}
