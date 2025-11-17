package com.levelupgamer.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.database.sqlite.SQLiteConstraintException
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.model.RegistrationUiState
import com.levelupgamer.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    // Métodos para actualizar estado (Unidireccional Data Flow)
    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, nombreError = null) }
    }
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }
    fun onEdadChange(edad: String) {
        // Solo permitir números
        if (edad.all { it.isDigit() }) {
            _uiState.update { it.copy(edad = edad, edadError = null) }
        }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }
    fun onConfirmPasswordChange(confirm: String) {
        _uiState.update { it.copy(confirmPassword = confirm, confirmPasswordError = null) }
    }

    fun registerUser() {
        // 1. Validación Centralizada
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            try {
                val state = _uiState.value

                // Lógica de negocio: Detectar alumno Duoc
                val isDuoc = state.email.endsWith("@duoc.cl", true) ||
                        state.email.endsWith("@duocuc.cl", true)

                val newUser = User(
                    nombre = state.nombre,
                    email = state.email,
                    passwordHash = state.password,
                    isDuocUser = isDuoc
                )

                userRepository.createUser(newUser)
                _uiState.update { it.copy(isLoading = false) }
                _registrationSuccess.value = true

            } catch (e: SQLiteConstraintException) {
                _uiState.update { it.copy(isLoading = false, generalError = "El email ya está registrado.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, generalError = "Error inesperado: ${e.message}") }
            }
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value
        var isValid = true

        // Validaciones específicas
        val nombreError = if (state.nombre.isBlank()) "El nombre es obligatorio" else null

        val emailError = if (state.email.isBlank()) "El email es obligatorio"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) "Formato de email inválido"
        else null

        val edadNum = state.edad.toIntOrNull()
        val edadError = if (state.edad.isBlank()) "La edad es obligatoria"
        else if (edadNum == null || edadNum < 18) "Debes ser mayor de 18 años"
        else null

        val passwordError = if (state.password.length < 6) "Mínimo 6 caracteres" else null

        val confirmError = if (state.confirmPassword != state.password) "Las contraseñas no coinciden" else null

        // Si hay algún error, fallamos
        if (nombreError != null || emailError != null || edadError != null || passwordError != null || confirmError != null) {
            isValid = false
        }

        // Actualizamos la UI con los errores encontrados
        _uiState.update {
            it.copy(
                nombreError = nombreError,
                emailError = emailError,
                edadError = edadError,
                passwordError = passwordError,
                confirmPasswordError = confirmError
            )
        }
        return isValid
    }
}