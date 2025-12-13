plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.productos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.productos"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = false
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            // Excluir los archivos LICENSE.md duplicados que causan el conflicto
            excludes += "META-INF/LICENSE.md"

            // A veces también es necesario excluir estos otros archivos comunes:
            excludes += "META-INF/LICENSE-apache-2.0.txt"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
    tasks.withType<Test>().configureEach {
        enabled = false
    }
}

dependencies {

    // Retrofit y Gson (ya las debes tener)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // DataStore para persistencia
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // ✅ AGREGAR ESTA LÍNEA para logs HTTP
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.0")

    // para algunas animaciones (hay unas en navegacion)
    implementation("com.google.accompanist:accompanist-navigation-animation:0.36.0")

    // ===== Compose (usa BOM del version catalog) =====
    // Material 3 (para NavigationBar y demás componentes modernos)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3) // Si tu catalog también expone material3 como libs.androidx.material3, deja solo uno (no ambos)
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1") // ===== Material3 window size class (opcional) =====
    // ⭐ Compose BOM actualizado (incluye Material3 1.3.x)
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // implementation("androidx.activity:activity-compose:1.11.0") // ← Usa solo uno: este o libs.androidx.activity.compose

    // dependencias para la cámaraX
    // ===== CameraX =====
    implementation("androidx.camera:camera-camera2:1.5.0")
    implementation("androidx.camera:camera-lifecycle:1.5.0")
    implementation("androidx.camera:camera-view:1.5.0")
    // Para la vista previa en compose (o usa AndroidView)
    // implementation("androidx.camera:camera-compose:1.0.0-alpha02") // ← Quita la super antigua camera-compose alpha para evitar choques:
    // ===== Compose UI =====
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material3:material3:1.2.1")
    // ⭐ aquí viene menuAnchor()
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material:material-icons-extended")

    // ===== Navigation =====
    implementation("androidx.navigation:navigation-compose:2.8.3")
    // Para manejar los permisos fácilmente
    // ===== Accompanist permissions =====
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // Dependencia para cargar la imagen después de ser capturada
    // ===== Coil =====
    implementation("io.coil-kt:coil-compose:2.1.0")
    // ===== ViewModel / Lifecycle =====
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // SDK principal de Mapbox (incluye todoo, incluso el location component)
    implementation("com.mapbox.maps:android-ndk27:11.16.2")
    implementation("com.mapbox.extension:maps-compose-ndk27:11.16.2")


    // --- Coroutines (solo si vas a usar funciones suspend o flujos) ---
    // ===== Coroutines =====
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ===== Retrofit =====
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // --- Compose interop y coroutines ---
    implementation("androidx.compose.ui:ui-viewbinding:1.7.0")


    // (No uses kotlinx-coroutines-play-services porque es para Google Tasks API)


    // Navegación entre pantallas (para usar NavController)
    // ===== Navigation (una sola vez) =====
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Para recordar estado (como rememberSaveable)
    // ===== Runtime saveable (solo si lo necesitas) =====
    implementation("androidx.compose.runtime:runtime-saveable") // Si NO está en tu catalog, puedes dejarlo así o moverlo a catalog. Si lo mantienes, mejor sin fijar versión si tu catalog ya lo trae.

    // Opcional: iconos de Material (para los íconos bonitos)
    // ===== Material Icons extendidos (alineados con BOM) =====
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.animation)

    // dependencias para la base de datos (clase 17-10-25)
    // ===== Room (solo KSP, sin annotationProcessor) =====
    // Room
    val roomVersion = "2.8.1" // la más reciente
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // annotationProcessor("androidx.room:room-compiler:$roomVersion") // ← eliminar (Kotlin usa KSP)
    implementation("androidx.room:room-ktx:$roomVersion") // Soporte para Coroutines

    // ViewModel y LiveData (si no usas StateFlow)
    // ===== Lifecycle =====
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Pruebas Unitarias

    testImplementation("io.mockk:mockk:1.13.8")
    // Si también haces pruebas instrumentadas (androidTest),
    androidTestImplementation("io.mockk:mockk-android:1.13.8")


    // Dependencias base de AndroidX Test (Versiones actualizadas y compatibles
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



}