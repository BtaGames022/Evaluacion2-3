package com.levelupgamer.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.levelupgamer.app.navigation.BottomNavItems

/**
 * Pantalla principal que contiene el Scaffold, la barra de navegación inferior
 * y el NavHost anidado para Home y Perfil.
 *
 * Acepta una función 'onLogout' para pasarla a la pantalla de Perfil.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit // <-- ACEPTA LA FUNCIÓN DE LOGOUT
) {
    // Controlador de navegación para el contenido INTERNO (Home, Perfil)
    val contentNavController = rememberNavController()

    // Lista de items de la barra
    val navItems = listOf(
        BottomNavItems.Home,
        BottomNavItems.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar { // Barra de navegación inferior
                val navBackStackEntry by contentNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            contentNavController.navigate(screen.route) {
                                popUpTo(contentNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // --- NavHost Anidado ---
        // Este NavHost cambia el contenido *dentro* del Scaffold
        NavHost(
            navController = contentNavController,
            startDestination = BottomNavItems.Home.route,
            modifier = Modifier.padding(innerPadding) // Aplica el padding del Scaffold
        ) {
            composable(BottomNavItems.Home.route) {
                HomeScreen() // Llama a la pantalla del catálogo
            }
            composable(BottomNavItems.Profile.route) {
                // --- CAMBIO AQUÍ ---
                // Pasa la función de logout a la ProfileScreen
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}