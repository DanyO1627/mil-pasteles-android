package com.example.productos.screen.eve

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.productos.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.PasswordVisualTransformation


//  LISTA COMPLETA DE REGIONES Y COMUNAS DE CHILE
val regiones = listOf(
    "Arica y Parinacota", "Tarapacá", "Antofagasta", "Atacama", "Coquimbo",
    "Valparaíso", "Región Metropolitana", "O'Higgins", "Maule", "Ñuble",
    "Biobío", "La Araucanía", "Los Ríos", "Los Lagos", "Aysén", "Magallanes"
)

val comunas = mapOf(
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
        "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla",
        "Peralillo", "Placilla", "Pumanque", "Santa Cruz"
    ),
    "Maule" to listOf(
        "Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco",
        "Pencahue", "Río Claro", "San Clemente", "San Rafael", "Cauquenes", "Chanco",
        "Pelluhue", "Curicó", "Hualañé", "Licantén", "Molina", "Rauco",
        "Romeral", "Sagrada Familia", "Teno", "Vichuquén", "Linares", "Colbún",
        "Longaví", "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"
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
        "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel",
        "Yumbel", "Alto Biobío", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Lebu",
        "Los Álamos", "Tirúa"
    ),
    "La Araucanía" to listOf(
        "Temuco", "Carahue", "Cholchol", "Cunco", "Curarrehue", "Freire",
        "Galvarino", "Gorbea", "Lautaro", "Loncoche", "Melipeuco", "Nueva Imperial",
        "Padre Las Casas", "Perquenco", "Pitrufquén", "Pucón", "Saavedra", "Teodoro Schmidt",
        "Toltén", "Vilcún", "Villarrica", "Angol", "Collipulli", "Curacautín", "Ercilla",
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
        "Estación Central", "Huechuraba", "Independencia", "La Cisterna",
        "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes",
        "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa",
        "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel",
        "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Joaquín",
        "San Miguel", "San Ramón", "Vitacura", "Puente Alto", "Pirque",
        "San José de Maipo", "Colina", "Lampa", "Tiltil", "Buin",
        "Calera de Tango", "Paine", "San Bernardo", "Melipilla", "Alhué",
        "Curacaví", "María Pinto", "San Pedro", "Talagante", "El Monte",
        "Isla de Maipo", "Padre Hurtado", "Peñaflor"
    )
)


//  PANTALLA COMPLETA DE REGISTRO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenRegistroUsuario(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }

    var expandedRegion by remember { mutableStateOf(false) }
    var expandedComuna by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA6B8),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFFF8F9))
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmarClave,
                onValueChange = { confirmarClave = it },
                label = { Text("Confirmar contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            // REGIONES
            ExposedDropdownMenuBox(
                expanded = expandedRegion,
                onExpandedChange = { expandedRegion = it }
            ) {
                OutlinedTextField(
                    value = region,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Región") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedRegion,
                    onDismissRequest = { expandedRegion = false }
                ) {
                    regiones.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                region = it
                                comuna = ""
                                expandedRegion = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            //  COMUNAS
            ExposedDropdownMenuBox(
                expanded = expandedComuna,
                onExpandedChange = { expandedComuna = it }
            ) {
                OutlinedTextField(
                    value = comuna,
                    onValueChange = {},
                    readOnly = true,
                    enabled = region.isNotEmpty(),
                    label = { Text("Comuna") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedComuna) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedComuna,
                    onDismissRequest = { expandedComuna = false }
                ) {
                    comunas[region]?.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                comuna = it
                                expandedComuna = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // BOTÓN Registrar
            Button(
                onClick = {
                    scope.launch {
                        when {
                            nombre.isBlank() -> snackbarHostState.showSnackbar("Ingresa tu nombre")
                            !email.contains("@") -> snackbarHostState.showSnackbar("Correo inválido")
                            clave.length < 6 -> snackbarHostState.showSnackbar("La contraseña debe tener mínimo 6 caracteres")
                            clave != confirmarClave -> snackbarHostState.showSnackbar("Las contraseñas no coinciden")
                            region.isBlank() -> snackbarHostState.showSnackbar("Selecciona una región")
                            comuna.isBlank() -> snackbarHostState.showSnackbar("Selecciona una comuna")
                            else -> {
                                usuarioViewModel.registrarUsuario(
                                    nombre = nombre,
                                    email = email,
                                    clave = clave,
                                    region = region,
                                    comuna = comuna
                                ) { ok ->
                                    scope.launch {
                                        if (ok) {
                                            snackbarHostState.showSnackbar("Usuario registrado con éxito 🎉")
                                            navController.popBackStack()
                                        } else {
                                            snackbarHostState.showSnackbar("Correo ya registrado ❌")
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()

                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB9405A))
            ) {
                Text("Registrar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
