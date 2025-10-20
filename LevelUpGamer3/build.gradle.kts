// build.gradle.kts (Project)
plugins {
    // Registra los plugins del catálogo
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}