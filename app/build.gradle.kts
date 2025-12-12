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

    // ⭐ Compose BOM actualizado (incluye Material3 1.3.x)
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // ===== Core Android =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

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

    // ===== ViewModel / Lifecycle =====
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // ===== Coroutines =====
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // ===== Retrofit =====
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ===== Room =====
    val roomVersion = "2.8.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // ===== Mapbox =====
    implementation("com.mapbox.maps:android-ndk27:11.16.2")
    implementation("com.mapbox.extension:maps-compose-ndk27:11.16.2")

    // ===== CameraX =====
    implementation("androidx.camera:camera-camera2:1.5.0")
    implementation("androidx.camera:camera-lifecycle:1.5.0")
    implementation("androidx.camera:camera-view:1.5.0")

    // ===== Accompanist =====
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // ===== Coil (imágenes) =====
    implementation("io.coil-kt:coil-compose:2.6.0")

    // ===== Test =====
    // ===== Testing =====
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
