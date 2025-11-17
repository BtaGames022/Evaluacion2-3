package com.levelupgamer.app.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star // <--- IMPORTANTE: Para el ícono de Ofertas
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
// --- IMPORTS DE TU PROYECTO ---
import com.levelupgamer.app.navigation.AppScreens
import com.levelupgamer.app.viewmodel.AppViewModelProvider // <--- IMPORTANTE: Fábrica de ViewModels
import com.levelupgamer.app.viewmodel.ProductDetailViewModel
import com.levelupgamer.app.viewmodel.ProductDetailViewModelFactory

// Clase de datos para los ítems del menú
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

    // Definición de los ítems del menú inferior
    val bottomNavItems = listOf(
        BottomNavItem(AppScreens.HomeTab.route, Icons.Default.Home, "Inicio"),
        BottomNavItem(AppScreens.CartTab.route, Icons.Default.ShoppingCart, "Carrito"),

        // Pestaña Blog
        BottomNavItem(AppScreens.PostScreen.route, Icons.Default.Article, "Blog"),

        // Pestaña Ofertas (Microservicio) - Si da error aquí, revisa el import de Icons.Default.Star arriba
        BottomNavItem(AppScreens.MicroserviceScreen.route, Icons.Default.Star, "Ofertas"),

        BottomNavItem(AppScreens.ProfileTab.route, Icons.Default.Person, "Perfil")
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = mainNavController, items = bottomNavItems)
        }
    ) { paddingValues ->
        // Contenedor de navegación principal
        MainNavHost(
            mainNavController = mainNavController,
            onLogout = onLogout,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun MainNavHost(
    mainNavController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = mainNavController,
        startDestination = AppScreens.HomeTab.route,
        modifier = modifier
    ) {
        // 1. Inicio
        composable(AppScreens.HomeTab.route) {
            HomeScreen(
                onProductClick = { productId ->
                    mainNavController.navigate(AppScreens.ProductDetail.createRoute(productId))
                }
            )
        }

        // 2. Carrito
        composable(AppScreens.CartTab.route) {
            CartScreen()
        }

        // 3. Blog
        composable(AppScreens.PostScreen.route) {
            PostScreen()
        }

        // 4. Ofertas (Microservicio) - Si da error, asegúrate de haber creado el archivo MicroserviceScreen.kt en la misma carpeta
        composable(AppScreens.MicroserviceScreen.route) {
            MicroserviceScreen()
        }

        // 5. Perfil
        composable(AppScreens.ProfileTab.route) {
            ProfileScreen(onLogout = onLogout)
        }

        // 6. Detalle de Producto
        composable(
            route = AppScreens.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId == null) {
                mainNavController.popBackStack()
                return@composable
            }

            val application = LocalContext.current.applicationContext as Application
            val viewModel: ProductDetailViewModel = viewModel(
                factory = ProductDetailViewModelFactory(application, productId)
            )

            ProductDetailScreen(
                viewModel = viewModel,
                onBack = { mainNavController.popBackStack() }
            )
        }
    }
}

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