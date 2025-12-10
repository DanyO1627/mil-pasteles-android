package com.example.productos.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = RosaBoton,
    secondary = CafeTexto,
    background = RosaFondo,
    onPrimary = Color.White
)

private val DarkColors = darkColorScheme(
    primary = RosaBoton,
    secondary = CafeTexto,
    background = RosaFondo,
    onPrimary = Color.White
)

@Composable
fun PasteleriaMilSaboresTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}