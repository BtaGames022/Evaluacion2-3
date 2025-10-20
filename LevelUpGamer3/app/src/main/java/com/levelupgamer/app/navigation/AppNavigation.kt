package com.levelupgamer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController // Importa NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.levelupgamer.app.ui.screens.LoginScreen
// --- ¡ASEGÚRATE DE QUE ESTA LÍNEA ESTÉ PRESENTE! ---
import com.levelupgamer.app.ui.screens.MainScreen
import com.levelupgamer.app.ui.screens.RegistrationScreen
// Asegúrate de que esta importación también esté
import com.levelupgamer.app.navigation.AppScreens


private val Any.route: String
    get() {
        TODO()
    }

/**
 * Gestiona el gráfico de navegación principal de la app.
 */
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController() // Controlador de navegación

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route // La app empieza en Login
    ) {

        // --- Pantalla de Login ---
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Navega a MainScreen y limpia la pila
                    navController.navigate(AppScreens.MainScreen.route) {
                        // --- Sintaxis REVISADA para popUpTo ---
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true // Evita múltiples instancias
                    }
                },
                onNavigateToRegister = {
                    // Navega a la pantalla de registro
                    navController.navigate(AppScreens.RegistrationScreen.route)
                }
            )
        }

        // --- Pantalla de Registro ---
        composable(AppScreens.RegistrationScreen.route) {
            RegistrationScreen(
                onRegisterSuccess = {
                    // Regresa a Login después de registrarse
                    navController.popBackStack()
                },
                onBackToLogin = {
                    // Regresa a Login
                    navController.popBackStack()
                }
            )
        }

        // --- Pantalla Principal (Contiene la BottomBar) ---
        composable(AppScreens.MainScreen.route) {
            // Le pasamos la lógica de logout a MainScreen
            MainScreen(
                onLogout = {
                    // Navega de regreso a Login y limpia toda la pila
                    navController.navigate(AppScreens.LoginScreen.route) {
                        // --- Sintaxis REVISADA para popUpTo ---
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true // Evita múltiples instancias
                    }
                }
            )
        }
    }
}