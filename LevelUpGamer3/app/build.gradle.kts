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
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // --- ARREGLO DEL ERROR "initializer" (IMPORTANTE) ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    // --- CONEXIÓN API REST (RETROFIT) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // --- OTRAS DEPENDENCIAS YA EXISTENTES ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
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
    // -------------------------------------------
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    // -------------------------------------------

    // Asegúrate de tener también estas (ya deberían estar):
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // ESTA ES LA CLAVE. Sin "-ktx", "initializer" no existe.
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Asegúrate de tener también esta para Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
}