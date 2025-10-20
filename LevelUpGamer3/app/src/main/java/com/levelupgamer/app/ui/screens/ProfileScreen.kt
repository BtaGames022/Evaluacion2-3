package com.levelupgamer.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // <-- Import needed for remember
import androidx.compose.runtime.remember // <-- Import needed for remember
import androidx.compose.runtime.setValue // <-- Import needed for remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.app.ui.components.ImagenInteligente
import com.levelupgamer.app.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit, // Función para navegar al Login
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val logoutSuccess by viewModel.logoutSuccess.collectAsState()

    // ---- INICIO DE LÓGICA DE RECURSOS NATIVOS (Guía 13) ----

    val context = LocalContext.current // Contexto necesario para permisos y launchers

    // --- 1. Launcher para la Galería (GetContent) ---
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() // Pide seleccionar contenido
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onImageSelectedFromGallery(uri)
        }
    }

    // --- 2. Launcher para la Cámara (TakePicture) ---
    // Variable local para almacenar la URI temporal (usando remember)
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture() // Pide tomar una foto
    ) { success: Boolean ->
        if (success) {
            // Si la foto fue tomada con éxito, notificamos al ViewModel
            viewModel.onImageCaptured()
        }
    }

    // --- 3. Launcher para solicitar Permisos (Cámara) ---
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, lanzar la cámara
            val uri = viewModel.getTempImageUri()
            tempCameraUri = uri // Guardamos la referencia
            cameraLauncher.launch(uri)
        } else {
            // TODO: Mostrar un mensaje al usuario indicando que el permiso es necesario
        }
    }

    // --- CORRECCIÓN IMPLEMENTADA ---
    // Función helper para lanzar la cámara (solicita permiso si es necesario)
    fun launchCamera(context: Context) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                // Permiso ya concedido
                // 1. Obtenemos la URI temporal del ViewModel
                val uri = viewModel.getTempImageUri()
                // 2. Guardamos la referencia localmente (necesario para el launcher)
                tempCameraUri = uri
                // 3. Lanzamos la cámara usando la URI local segura
                cameraLauncher.launch(uri)
            }
            else -> {
                // Solicitar permiso
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    // --- FIN CORRECCIÓN ---

    // ---- FIN DE LÓGICA DE RECURSOS NATIVOS ----


    // Escucha el evento de logout para navegar
    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            onLogout()
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(24.dp))

            // Muestra la ImagenInteligente
            ImagenInteligente(
                modifier = Modifier.padding(bottom = 16.dp),
                imageUri = uiState.profileImageUri
            )

            // Botones para Galería y Cámara
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Galería")
                }
                Button(onClick = { launchCamera(context) }) {
                    Text("Cámara")
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(uiState.nombre, style = MaterialTheme.typography.titleLarge)
            Text(uiState.email, style = MaterialTheme.typography.bodyMedium)

            if (uiState.esUsuarioDuoc) {
                Text(
                    "Usuario Duoc (¡20% Dcto!)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.weight(1f)) // Empuja el botón de logout hacia abajo

            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}