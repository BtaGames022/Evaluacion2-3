package com.levelupgamer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.levelupgamer.app.ui.screens.LoginScreen
import com.levelupgamer.app.ui.screens.MainScreen // <-- ¡¡IMPORTACIÓN AÑADIDA!!
import com.levelupgamer.app.ui.screens.RegistrationScreen


/**
 * Gestiona el gráfico de navegación principal de la app.
 */
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ) {

        // --- Pantalla de Login ---
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppScreens.MainScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            this.inclusive = true // <-- SINTAXIS CORREGIDA
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppScreens.RegistrationScreen.route)
                }
            )
        }

        // --- Pantalla de Registro ---
        composable(AppScreens.RegistrationScreen.route) {
            RegistrationScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // --- Pantalla Principal (Contiene la BottomBar) ---
        composable(AppScreens.MainScreen.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(AppScreens.LoginScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            this.inclusive = true // <-- SINTAXIS CORREGIDA
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}