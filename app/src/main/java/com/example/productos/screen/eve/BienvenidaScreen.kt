package com.example.productos.screen.eve



import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.productos.R
import com.example.productos.ui.theme.*
@Composable
fun BienvenidaScreen(navController: NavHostController)

 {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RosaFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Pastelería",
            modifier = Modifier
                .size(320.dp)
                .padding(bottom = 32.dp)
        )

        // Título
        Text(
            text = "PASTELERÍA",
            color = CafeTexto,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // Subtítulo
        Text(
            text = "Mil Sabores",
            color = CafeTexto,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 36.dp)
        )

        // Botón principal
        Button(
            onClick = {
                navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(containerColor = RosaBoton),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .height(56.dp)
                .widthIn(min = 220.dp)
        ) {
            Text(
                text = " Entrar a la dulzura",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
 }



