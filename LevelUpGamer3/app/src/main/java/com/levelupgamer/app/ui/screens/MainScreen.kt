package com.levelupgamer.app.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
// --- ¡IMPORTANTE! Añade este import ---
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.levelupgamer.app.navigation.AppScreens
import com.levelupgamer.app.viewmodel.ProductDetailViewModel
import com.levelupgamer.app.viewmodel.ProductDetailViewModelFactory

// ... (data class BottomNavItem no cambia) ...
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val mainNavController = rememberNavController()

    // --- Definición de los ítems de la barra inferior (ACTUALIZADO) ---
    val bottomNavItems = listOf(
        BottomNavItem(AppScreens.HomeTab.route, Icons.Default.Home, "Inicio"),
        BottomNavItem(AppScreens.CartTab.route, Icons.Default.ShoppingCart, "Carrito"),
        // --- AÑADIR ESTA LÍNEA ---
        BottomNavItem(AppScreens.PostScreen.route, Icons.Default.Article, "Blog"),
        BottomNavItem(AppScreens.ProfileTab.route, Icons.Default.Person, "Perfil")
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = mainNavController, items = bottomNavItems)
        }
    ) { paddingValues ->
        // --- NavHost anidado (Ahora incluye DETALLE) ---
        MainNavHost(
            mainNavController = mainNavController,
            onLogout = onLogout,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * Este es el NavHost que controla el contenido PRINCIPAL (Pestañas y Detalle).
 */
@Composable
fun MainNavHost(
    mainNavController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = mainNavController,
        startDestination = AppScreens.HomeTab.route, // Ruta inicial
        modifier = modifier
    ) {
        // --- Tab de Inicio ---
        composable(AppScreens.HomeTab.route) {
            HomeScreen(
                // Al hacer clic en un producto, navegar a Detalles
                onProductClick = { productId ->
                    mainNavController.navigate(AppScreens.ProductDetail.createRoute(productId))
                }
            )
        }

        // --- Tab de Carrito (NUEVO) ---
        composable(AppScreens.CartTab.route) {
            CartScreen()
        }

        // --- Tab de Perfil ---
        composable(AppScreens.ProfileTab.route) {
            ProfileScreen(onLogout = onLogout)
        }

        // --- AÑADIR ESTE COMPOSABLE ---
        // --- Tab de Blog (API) ---
        composable(AppScreens.PostScreen.route) {
            PostScreen() // El ViewModel se inyectará automáticamente
        }
        // --- FIN DE LA ADICIÓN ---

        // --- Pantalla de Detalle de Producto (NUEVO) ---
        composable(
            route = AppScreens.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Obtener el productId de la ruta
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId == null) {
                // Manejar error (ej. volver atrás)
                mainNavController.popBackStack()
                return@composable
            }

            // Usar la Factory para crear el ViewModel con el productId
            val application = LocalContext.current.applicationContext as Application
            val viewModel: ProductDetailViewModel = viewModel(
                factory = ProductDetailViewModelFactory(application, productId)
            )

            ProductDetailScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() } // Botón para volver
            )
        }
    }
}

// ... (El Composable BottomNavigationBar no cambia)
@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
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