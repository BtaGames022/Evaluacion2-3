package com.levelupgamer.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Objeto sellado para la barra de navegación inferior
sealed class BottomNavItems(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomNavItems(
        route = "catalog_home", // Ruta interna
        icon = Icons.Default.Home,
        label = "Catálogo"
    )

    data object Profile : BottomNavItems(
        route = "user_profile", // Ruta interna
        icon = Icons.Default.Person,
        label = "Perfil"
    )
}