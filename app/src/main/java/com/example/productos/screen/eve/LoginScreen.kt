package com.example.productos.screen.eve

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.productos.R
import com.example.productos.ui.theme.*
import com.example.productos.viewmodel.UsuarioViewModel
import kotlinx.coroutines.*

@Composable
fun LoginScreen(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RosaFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )


    {

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = { navController.navigate("home") },
                enabled = !cargando,
                colors = ButtonDefaults.buttonColors(containerColor = RosaBoton),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Text("volver", color = Color.White, fontSize = 18.sp)
            }
        }



        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Pastelería",
            modifier = Modifier
                .size(180.dp)
                .padding(bottom = 16.dp)
        )

        // Título
        Text(
            text = "Iniciar sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = CafeTexto,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Nombre o correo electrónico") },
            placeholder = { Text("Correo o usuario") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RosaBoton,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = CafeTexto
            ),
            textStyle = TextStyle(color = CafeTexto, fontSize = 16.sp)
        )

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RosaBoton,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = CafeTexto
            ),
            textStyle = TextStyle(color = CafeTexto, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón con carga visible
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(
                        context,
                        "Por favor completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    cargando = true

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1300)
                        cargando = false

                        val valido = usuarioViewModel.validarCredenciales(email, password)

                        if (valido) {
                            Toast.makeText(
                                context,
                                "¡Inicio de sesión exitoso! 🎉",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Credenciales incorrectas ❌",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            enabled = !cargando,
            colors = ButtonDefaults.buttonColors(containerColor = RosaBoton),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            if (cargando) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 8.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                    Text("Cargando...", color = Color.White, fontSize = 18.sp)
                }
            } else {
                Text("Entrar", color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto de registro
        Text(
            buildAnnotatedString {
                append("¿No tienes cuenta? ")
                withStyle(style = SpanStyle(color = RosaBoton, fontWeight = FontWeight.Bold)) {
                    append("Regístrate")
                }
            },
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                navController.navigate("registroUsuario")
            }
        )
    }
}
