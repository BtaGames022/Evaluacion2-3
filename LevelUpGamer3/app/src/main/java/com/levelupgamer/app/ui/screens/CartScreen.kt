package com.levelupgamer.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.app.model.CartProduct
import com.levelupgamer.app.viewmodel.CartViewModel
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
fun CartScreen(
    viewModel: CartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // ... (El diálogo de Alerta no cambia) ...
    if (uiState.showCheckoutSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCheckoutDialog() },
            title = { Text("¡Compra Exitosa!") },
            text = { Text("Tu compra ha sido realizada con éxito.") },
            confirmButton = {
                Button(onClick = { viewModel.dismissCheckoutDialog() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        // ... (TopBar no cambia) ...
        topBar = {
            TopAppBar(title = { Text("Mi Carrito") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // ... (La lógica de isLoading, errorMessage y carrito vacío no cambia) ...

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.errorMessage != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
                }
            } else if (uiState.cartProducts.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // Lista de productos en el carrito
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.cartProducts) { cartProduct ->
                        CartItemCard(
                            cartProduct = cartProduct,
                            onIncrease = { viewModel.increaseQuantity(cartProduct.product.codigo) },
                            onDecrease = { viewModel.decreaseQuantity(cartProduct.product.codigo) },
                            onRemove = { viewModel.removeItem(cartProduct.product.codigo) }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                // --- Resumen del total (ACTUALIZADO CON DESGLOSE) ---

                // 1. Subtotal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Subtotal:", style = MaterialTheme.typography.titleMedium)
                    Text(
                        formatCurrency(uiState.subtotal),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.height(4.dp))

                // 2. Descuento (Solo si aplica)
                if (uiState.isDuocUser && uiState.discount > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Descuento Duoc (20%):",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "- ${formatCurrency(uiState.discount)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // 3. Total Final
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total:", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        formatCurrency(uiState.totalAmount), // Este es el total CON descuento
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                // --- FIN DEL RESUMEN ACTUALIZADO ---

                Spacer(Modifier.height(16.dp))

                // --- Botones de acción (no cambian) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { viewModel.clearCart() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Vaciar Carrito")
                    }
                    Button(onClick = { viewModel.onCheckoutClicked() }) {
                        Text("Pagar")
                    }
                }
            }
        }
    }
}

// ... (El Composable CartItemCard no cambia) ...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemCard(
    cartProduct: CartProduct,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cartProduct.product.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(
                    formatCurrency(cartProduct.product.precio),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease) {
                    Icon(Icons.Default.Remove, contentDescription = "Quitar uno")
                }
                Text("${cartProduct.quantity}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir uno")
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}