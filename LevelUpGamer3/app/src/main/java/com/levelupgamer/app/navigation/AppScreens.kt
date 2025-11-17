package com.levelupgamer.app.navigation

/**
 * Clase sellada para definir las rutas de navegación de forma segura.
 */
sealed class AppScreens(val route: String) {
    // 1. Flujo de Autenticación
    data object LoginScreen : AppScreens("login_screen")
    data object RegistrationScreen : AppScreens("registration_screen")

    // 2. Pantalla Principal (Contenedor de pestañas)
    data object MainScreen : AppScreens("main_screen")

    // 3. Pestañas de la Barra de Navegación Inferior
    object HomeTab : AppScreens("home_tab")
    object CartTab : AppScreens("cart_tab")
    object ProfileTab : AppScreens("profile_tab")
    data object PostScreen : AppScreens("post_screen") // <-- AÑADIR ESTA LÍNEA

    // 4. Pantalla de Detalle (con argumento)
    // Define la ruta base y el nombre del argumento
    object ProductDetail : AppScreens("product_detail/{productId}") {
        // Función helper para construir la ruta con el ID real
        fun createRoute(productId: String) = "product_detail/$productId"
    }
}