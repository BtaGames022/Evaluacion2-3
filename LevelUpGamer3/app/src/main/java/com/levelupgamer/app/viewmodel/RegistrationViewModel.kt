package com.levelupgamer.app.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.model.RegistrationUiState
import com.levelupgamer.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- CAMBIO 1: Extender de AndroidViewModel ---
class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    // --- CAMBIO 2: Obtener el repositorio desde la Application ---
    private val userRepository: UserRepository =
        (application as LevelUpGamerApplication).userRepository

    // --- Estado Interno (Mutable) ---
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    // --- CAMBIO 3: Añadir un Flow para notificar el éxito ---
    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()


    // --- Eventos (sin cambios) ---
    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, nombreError = null, generalError = null) }
    }
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, generalError = null) }
    }
    fun onEdadChange(edad: String) {
        _uiState.update { it.copy(edad = edad, edadError = null, generalError = null) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, generalError = null) }
    }
    fun onConfirmPasswordChange(confirm: String) {
        _uiState.update { it.copy(confirmPassword = confirm, confirmPasswordError = null, generalError = null) }
    }


    fun registerUser() {
        if (!validateForm()) {
            return
        }

        // --- CAMBIO 4: Lógica de registro real ---
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            try {
                val state = _uiState.value
                val isDuoc = state.email.endsWith("@duoc.cl", true) ||
                        state.email.endsWith("@duocuc.cl", true)

                // TODO: ¡¡¡ NUNCA guardes contraseñas en texto plano !!!
                // En una app real, aquí debes "hashear" la contraseña.
                // val passwordHash = bcrypt.hash(state.password)
                val passwordHash = state.password // Placeholder inseguro

                val newUser = User(
                    nombre = state.nombre,
                    email = state.email,
                    passwordHash = passwordHash,
                    isDuocUser = isDuoc
                )

                // Insertamos en la base de datos
                userRepository.createUser(newUser)

                // Si llegamos aquí, fue exitoso
                _uiState.update { it.copy(isLoading = false) }
                _registrationSuccess.value = true // ¡Disparamos el evento de éxito!

            } catch (e: SQLiteConstraintException) {
                // Error de email duplicado
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "El email ya está registrado."
                    )
                }
            } catch (e: Exception) {
                // Otro error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Ocurrió un error: ${e.message}"
                    )
                }
            }
        }
    }

    // Función de validación (sin cambios)
    private fun validateForm(): Boolean {
        // ... (el código de validación de 18 años, etc. es el mismo)
        val state = _uiState.value
        var isValid = true
        val nombreError = if (state.nombre.isBlank()) "El nombre es obligatorio" else null
        if (nombreError != null) isValid = false
        val emailError = if (state.email.isBlank()) "El email es obligatorio"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) "El email no es válido"
        else null
        if (emailError != null) isValid = false
        val edadNum = state.edad.toIntOrNull()
        val edadError = if (state.edad.isBlank()) "La edad es obligatoria"
        else if (edadNum == null) "La edad debe ser un número"
        else if (edadNum < 18) "Debes ser mayor de 18 años"
        else null
        if (edadError != null) isValid = false
        val passwordError = if (state.password.isBlank()) "La contraseña es obligatoria"
        else if (state.password.length < 6) "Debe tener al menos 6 caracteres"
        else null
        if (passwordError != null) isValid = false
        val confirmError = if (state.confirmPassword != state.password) "Las contraseñas no coinciden" else null
        if (confirmError != null) isValid = false
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