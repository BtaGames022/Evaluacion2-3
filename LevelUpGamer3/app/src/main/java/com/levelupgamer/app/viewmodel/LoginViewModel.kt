package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.SessionDataStore
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as LevelUpGamerApplication).userRepository

    // --- CAMBIO 1: Obtener el DataStore ---
    private val sessionDataStore: SessionDataStore =
        (application as LevelUpGamerApplication).sessionDataStore

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    // ... (onEmailChange, onPasswordChange sin cambios)
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, loginError = null) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, loginError = null) }
    }

    fun loginUser() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }
            try {
                val state = _uiState.value
                val user = userRepository.getUserByEmail(state.email)

                if (user == null || user.passwordHash != state.password) {
                    _uiState.update { it.copy(isLoading = false, loginError = "Credenciales incorrectas") }
                } else {
                    // --- CAMBIO 2: Guardar Sesión ---
                    // ¡Éxito! Guardamos el email en DataStore
                    sessionDataStore.saveUserEmail(user.email)

                    _uiState.update { it.copy(isLoading = false) }
                    _loginSuccess.value = true // Disparamos la navegación
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, loginError = "Error: ${e.message}") }
            }
        }
    }

    // ... (validateForm sin cambios)
    private fun validateForm(): Boolean {
        val state = _uiState.value
        var isValid = true
        val emailError = if (state.email.isBlank()) "El email es obligatorio"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) "Email no válido"
        else null
        if (emailError != null) isValid = false
        val passwordError = if (state.password.isBlank()) "La contraseña es obligatoria" else null
        if (passwordError != null) isValid = false
        _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }
        return isValid
    }
}