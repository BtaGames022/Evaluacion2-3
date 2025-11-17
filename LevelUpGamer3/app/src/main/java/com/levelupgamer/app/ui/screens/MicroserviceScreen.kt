package com.levelupgamer.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.app.viewmodel.AppViewModelProvider
import com.levelupgamer.app.viewmodel.SpringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MicroserviceScreen(
    viewModel: SpringViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ofertas (Spring Boot)") },
                actions = {
                    IconButton(onClick = { viewModel.fetchBackendProducts() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.error != null -> Text("Error: ${uiState.error}", modifier = Modifier.align(Alignment.Center).padding(16.dp))
                else -> LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(uiState.products) { product ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(product.name, fontWeight = FontWeight.Bold)
                                Text("$${product.price}", color = MaterialTheme.colorScheme.primary)
                                Text(product.description)
                            }
                        }
                    }
                }
            }
        }
    }
}