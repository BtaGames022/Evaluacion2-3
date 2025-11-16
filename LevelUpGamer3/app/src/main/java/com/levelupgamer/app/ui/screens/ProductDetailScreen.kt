package com.levelupgamer.app.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.app.viewmodel.ProductDetailViewModel
import com.levelupgamer.app.viewmodel.ProductDetailViewModelFactory
import java.text.NumberFormat
import java.util.Locale

// Helper (Mover a un archivo utils si se repite)
private fun formatCurrency(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(price)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.nombre ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (product != null) {
                FloatingActionButton(
                    onClick = { viewModel.addToCart(product.codigo) }
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Añadir al Carrito")
                }
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (product == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ... (Imagen y Precio/Nombre) ...
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: Cargar imagen real del producto
                        Text(product.categoria, style = MaterialTheme.typography.headlineSmall)
                    }
                }
                item {
                    Text(product.nombre, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        formatCurrency(product.precio),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                // --- ¡¡PETICIÓN 1 IMPLEMENTADA (UI)!! ---
                // Muestra la disponibilidad por región actualizada.
                item {
                    Text(
                        "Disponibilidad por Región",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                if (uiState.stockByRegion.isEmpty()) {
                    item {
                        Text("No hay información de stock disponible.")
                    }
                } else {
                    // Muestra la lista de todas las regiones
                    items(uiState.stockByRegion) { stock ->
                        StockInfoRow(region = stock.region, stock = stock.stock)
                    }
                }
            }
        }
    }
}

/**
 * Composable actualizado para mostrar "Disponible (Cant.)" o "Fuera de Stock".
 */
@Composable
fun StockInfoRow(region: String, stock: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(region, style = MaterialTheme.typography.bodyLarge)

        // --- LÓGICA DE TEXTO Y COLOR ACTUALIZADA ---
        val (stockText, stockColor) = if (stock > 0) {
            "Disponible ($stock)" to MaterialTheme.colorScheme.primary // Color primario
        } else {
            "Fuera de Stock" to MaterialTheme.colorScheme.error // Color rojo
        }

        Text(
            stockText,
            style = MaterialTheme.typography.bodyLarge,
            color = stockColor,
            fontWeight = FontWeight.Bold
        )
    }
}