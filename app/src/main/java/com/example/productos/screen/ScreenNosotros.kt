package com.example.productos.screen

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.common.MapboxOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.location
import com.example.productos.R
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.annotations

import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenNosotros(navController: NavController) {
    val context = LocalContext.current

    // permisos de ubicación
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        locationPermissions.launchMultiplePermissionRequest()
    }

    val imagenesCarrusel = listOf(
        R.drawable.pasteleria_frente,
        R.drawable.evento,

        R.drawable.pasteleria_imagen,
        R.drawable.pastelera,
//        R.drawable.pastelera2,

    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sobre Nosotros",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4E342E)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFFA6B8)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(20.dp))

            // frase
            Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Explora un universo dulce. ¡Mil Sabores te espera!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF592D2D),
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Carrusel de imágenes
            Text(
                text = "Echa un vistazo a nuestro mundo dulce 🍰",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF592D2D),
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(imagenesCarrusel) { imagen ->
                    Image(
                        painter = painterResource(id = imagen),
                        contentDescription = "Imagen de pastelería",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(width = 180.dp, height = 140.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            // filosofía
            Text(
                text = "Nuestra filosofía",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF592D2D),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilosofiaItem(Icons.Default.Favorite, "Amor", "En cada receta y cada sonrisa.")
                FilosofiaItem(Icons.Default.Star, "Calidad", "Solo los mejores ingredientes.")
                FilosofiaItem(Icons.Default.Brush, "Creatividad", "Diseños únicos para ti.")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // direccion y mapa
            Text(
                text = "Encuéntranos en…",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color(0xFF592D2D),
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Av. Providencia 1234, Santiago",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontSize = 15.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                locationPermissions.allPermissionsGranted -> {
                    MapaMilSabores(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFFFF1F3))
                    )
                }
                locationPermissions.shouldShowRationale -> {
                    PermisosUI(
                        "Necesitamos acceso a tu ubicación para mostrar el mapa",
                        locationPermissions::launchMultiplePermissionRequest
                    )
                }
                else -> {
                    PermisosUI(
                        "Por favor, permite el acceso a tu ubicación para continuar",
                        locationPermissions::launchMultiplePermissionRequest
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = { navController.navigate("contacto") },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6B8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("📞 Contáctanos", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // TXT
            Text(
                text = "Gracias por ser parte de nuestra historia dulce 💕",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF6B3143),
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

// item de filosofia
@Composable
fun FilosofiaItem(icon: androidx.compose.ui.graphics.vector.ImageVector, titulo: String, texto: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(100.dp)) {
        Icon(
            icon,
            contentDescription = titulo,
            tint = Color(0xFFFFA6B8),
            modifier = Modifier.size(36.dp)
        )
        Text(titulo, fontWeight = FontWeight.Bold, color = Color(0xFF4E342E))
        Text(texto, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 12.sp)
    }
}

// mapa
@Composable
fun MapaMilSabores(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var mapView by remember { mutableStateOf<MapView?>(null) }

    remember {
        MapboxOptions.accessToken =
            "pk.eyJ1IjoiZnJlZGNhbXBvczEyMzAiLCJhIjoiY2xudTl2d2VrMDlpbzJrcWpnYnJkc3JqbCJ9.hjid1kkpkU37wvVJrj2pQg"
        true
    }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_START -> mapView?.onStart()
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> mapView?.onStop()
                androidx.lifecycle.Lifecycle.Event.ON_DESTROY -> mapView?.onDestroy()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    AndroidView(
        factory = {
            MapView(
                context,
                MapInitOptions(context = context, styleUri = Style.MAPBOX_STREETS)
            ).apply {
                mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    // coordenadas de providencia
                    val lat = -33.4263
                    val lng = -70.6169

                    // centra la camara a la direccion
                    mapboxMap.setCamera(
                        com.mapbox.maps.CameraOptions.Builder()
                            .center(com.mapbox.geojson.Point.fromLngLat(lng, lat))
                            .zoom(15.5) // un zoom más cercano al local
                            .build()
                    )

                    // sale el marcador
                    val annotationApi = annotations
                    val pointManager = annotationApi.createPointAnnotationManager()
                    val point = com.mapbox.geojson.Point.fromLngLat(lng, lat)
                    val pointOptions = com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("marker-15") // ícono por defecto de Mapbox

                    pointManager.create(pointOptions)

                    // gestos habilitados para el usaurio
                    gestures.updateSettings {
                        scrollEnabled = true
                        pinchToZoomEnabled = true
                        rotateEnabled = true
                        pitchEnabled = true
                    }

                    // ubicacion del usuario
                    location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }
                }
            }.also { mapView = it }
        },
        modifier = modifier
    )


}

// permisos
@Composable
fun PermisosUI(texto: String, onSolicitar: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text(texto, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSolicitar,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6B8))
        ) {
            Text("Conceder permisos", color = Color.White)
        }
    }
}
