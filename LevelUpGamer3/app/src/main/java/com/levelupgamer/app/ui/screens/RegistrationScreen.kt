package com.levelupgamer.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect // <-- Importante para la navegación
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelupgamer.app.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: RegistrationViewModel = viewModel() // Inyecta el ViewModel
) {
    // Observa el estado de la UI (campos, errores, carga)
    val uiState by viewModel.uiState.collectAsState()

    // Observa el evento de éxito de la navegación
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()

    // --- CÓDIGO AÑADIDO PARA NAVEGAR ---
    // Este bloque "escucha" la variable 'registrationSuccess'.
    // Si cambia a 'true', ejecuta el código de navegación.
    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            onRegisterSuccess() // Llama a la función lambda para navegar
        }
    }
    // --- FIN DEL CÓDIGO AÑADIDO ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    // Botón para volver a Login
                    IconButton(onClick = onBackToLogin) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Permite scroll
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Regístrate en Level-Up Gamer", style = MaterialTheme.typography.headlineSmall)

            // --- Campo Nombre ---
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre Completo") },
                isError = uiState.nombreError != null,
                supportingText = {
                    uiState.nombreError?.let { error -> Text(error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campo Email ---
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { error -> Text(error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campo Edad ---
            OutlinedTextField(
                value = uiState.edad,
                onValueChange = viewModel::onEdadChange,
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.edadError != null,
                supportingText = {
                    uiState.edadError?.let { error -> Text(error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campo Contraseña ---
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { error -> Text(error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Campo Confirmar Contraseña ---
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = uiState.confirmPasswordError != null,
                supportingText = {
                    uiState.confirmPasswordError?.let { error -> Text(error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // --- Botón de Registro ---
            Button(
                onClick = viewModel::registerUser,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }

            // Muestra un error general (ej. "Email ya existe")
            uiState.generalError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}