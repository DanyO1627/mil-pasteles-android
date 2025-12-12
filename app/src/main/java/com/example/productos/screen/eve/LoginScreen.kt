package com.example.productos.screen.eve

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.productos.R
import com.example.productos.viewmodel.UsuarioViewModel

// 🎨 Colores estilo Pastelería
val RosaFondo = Color(0xFFFFF4F7)
val RosaBoton = Color(0xFFB9405A)
val CafeTexto = Color(0xFF4A2C2A)

@Composable
fun LoginScreen(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RosaFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(45.dp))

        // 🧁 Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Pastelería",
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Iniciar sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTexto
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 📧 EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            placeholder = { Text("ejemplo@mail.cl") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔒 CONTRASEÑA
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(22.dp))

        // 🔘 BOTÓN DE INGRESAR
        Button(
            onClick = {
                if (email.isBlank() || clave.isBlank()) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    cargando = true

                    usuarioViewModel.login(email, clave) { ok ->
                        cargando = false

                        if (ok) {
                            Toast.makeText(context, "Bienvenido(a) 🎉", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Correo o contraseña incorrecta ❌", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RosaBoton)
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 3.dp
                )
            } else {
                Text("Entrar", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 🔗 ENLACE A REGISTRO
        Text(
            buildAnnotatedString {
                append("¿No tienes una cuenta? ")
                withStyle(style = SpanStyle(color = RosaBoton, fontWeight = FontWeight.Bold)) {
                    append("Regístrate")
                }
            },
            modifier = Modifier.clickable {
                navController.navigate("registroUsuario")
            },
            fontSize = 15.sp
        )
    }
}
