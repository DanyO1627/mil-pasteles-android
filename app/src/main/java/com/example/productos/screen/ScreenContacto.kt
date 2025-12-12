package com.example.productos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.productos.ui.utils.vibrarSuave
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContacto(navController: NavController) {

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Contáctanos",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4E342E)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                ),
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "¿Tienes dudas o comentarios?",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4E342E)
            )

            // Campo nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Campo correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Campo mensaje
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    when {
                        nombre.isBlank() || correo.isBlank() || mensaje.isBlank() -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch { snackbarHostState.showSnackbar("Completa todos los campos 📝") }
                        }
                        // Validación directa de correo simple y clara
                        !correo.contains("@") || !correo.contains(".") -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch { snackbarHostState.showSnackbar("⚠️ Correo inválido") }
                        }
                        else -> {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch {
                                snackbarHostState.showSnackbar("Mensaje enviado con éxito 💌")
                                // Espera breve para mostrar el mensaje antes de navegar
                                kotlinx.coroutines.delay(0)
                                // 🔁 Te envía de vuelta a la pantalla de "Nosotros"
                                navController.navigate(Destination.PERFIL.route) {
                                    popUpTo("contacto") { inclusive = true } // limpia el stack para no volver al formulario
                                }
                            }
                            nombre = ""
                            correo = ""
                            mensaje = ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373))
            ) {
                Text("Enviar mensaje", color = Color.White)
            }
        }
    }
}
