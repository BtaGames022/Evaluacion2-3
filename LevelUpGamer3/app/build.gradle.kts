// build.gradle.kts (Module :app)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Aplica KSP para Room
}

android {
    namespace = "com.levelupgamer.app" // Confirma tu package name
    compileSdk = 34

    defaultConfig {
        applicationId = "com.levelupgamer.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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

    // Configura Java 17, como piden las guías
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // --- AÑADIR ESTO (Guía 15) ---
    // Configura el runner de tests para usar JUnit 5 (necesario para Kotest)
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

dependencies {
    // --- CORE & COMPOSE ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // Ya incluye íconos base

    // --- 1. VIEWMODEL, LIFECYCLE, CORRUTINAS ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.android)

    // --- 2. NAVEGACIÓN ---
    implementation(libs.androidx.navigation.compose)

    // --- 3. PERSISTENCIA LOCAL: ROOM (SQLite) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Usa KSP para el compilador

    // --- 4. PERSISTENCIA LOCAL: DATASTORE ---
    implementation(libs.androidx.datastore.preferences)

    // --- 5. LIBRERÍA DE IMÁGENES (CÁMARA/GALERÍA) ---
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- 6. API REST (NUEVO - Guía 14) ---
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // --- 7. ÍCONOS EXTENDIDOS (Ya estaba) ---
    implementation(libs.androidx.compose.material.icons.extended)

    // --- TESTING ---
    testImplementation(libs.junit) // JUnit 4 base

    // --- TESTING (NUEVO - Guía 15) ---
    testImplementation(libs.kotest.runner.junit5) // Runner de Kotest
    testImplementation(libs.kotest.assertions.core) // Assertions (shouldBe)
    testImplementation(libs.junit.jupiter) // JUnit 5
    testImplementation(libs.mockk.core) // MockK
    testImplementation(libs.kotlinx.coroutines.test) // Para testear corrutinas

    // --- ANDROID TESTING (Existente) ---
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}