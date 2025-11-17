// build.gradle.kts (Module :app)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Aplica KSP para Room
}

android {
    namespace = "com.levelupgamer.app"
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

    // --- CONFIGURACIÓN DE FIRMA (COMENTADA TEMPORALMENTE) ---
    // Se ha comentado para solucionar el error de compilación.
    // Descomenta y rellena con tus datos cuando necesites firmar un APK de lanzamiento.
    /*
    signingConfigs {
        create("release") {
            // Asegúrate de que el archivo 'keystore.jks' esté dentro de la carpeta 'app'
            storeFile = file("keystore.jks")
            storePassword = "YOUR_PASSWORD_HERE"
            keyAlias = "YOUR_ALIAS_HERE"
            keyPassword = "YOUR_PASSWORD_HERE"
        }
    }
    */

    buildTypes {
        release {
            // --- SEGURIDAD Y OPTIMIZACIÓN (NUEVO) ---
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Descomenta la siguiente línea cuando configures tu firma.
            // signingConfig = signingConfigs.getByName("release")
        }
    }

    // ... (El resto de la configuración: compileOptions, kotlinOptions, etc. déjalo igual)
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
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // --- VIEWMODEL ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // --- CONEXIÓN API REST (RETROFIT) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // --- OTRAS DEPENDENCIAS ---
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- TESTING ---
    testImplementation(libs.junit)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk.core)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
