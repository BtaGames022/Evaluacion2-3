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
    data object HomeTab : AppScreens("home_tab")
    data object CartTab : AppScreens("cart_tab")
    data object ProfileTab : AppScreens("profile_tab")

    // Pestaña para el Blog (API Externa)
    data object PostScreen : AppScreens("post_screen")

    // --- ESTA ES LA LÍNEA CLAVE PARA TU ERROR ---
    data object MicroserviceScreen : AppScreens("microservice_screen")
    // -------------------------------------------

    // 4. Pantalla de Detalle (con argumento)
    data object ProductDetail : AppScreens("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
}