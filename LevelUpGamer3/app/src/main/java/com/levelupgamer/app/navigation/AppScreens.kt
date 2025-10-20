package com.levelupgamer.app.navigation

/**
 * Clase sellada para definir las rutas de navegación de forma segura.
 */
sealed class AppScreens(val route: String) {
    // Pantallas que no requieren login
    data object LoginScreen : AppScreens("login_screen")
    data object RegistrationScreen : AppScreens("registration_screen")

    // Pantalla principal que contiene la barra de navegación
    data object MainScreen : AppScreens("main_screen")
}