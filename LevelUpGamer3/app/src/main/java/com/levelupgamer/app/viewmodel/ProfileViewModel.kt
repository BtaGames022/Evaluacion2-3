package com.levelupgamer.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.FileProviderUtil
import com.levelupgamer.app.data.SessionDataStore
import com.levelupgamer.app.data.UserRepository
import com.levelupgamer.app.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository =
        (application as LevelUpGamerApplication).userRepository
    private val sessionDataStore: SessionDataStore =
        (application as LevelUpGamerApplication).sessionDataStore

    // --- CAMBIO 1: Obtener el helper ---
    private val fileProviderUtil: FileProviderUtil =
        (application as LevelUpGamerApplication).fileProviderUtil

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess.asStateFlow()

    init {
        loadUserData()
    }

    // --- CAMBIO 2: Lógica para la imagen (Guía 13) ---

    /**
     * Crea una URI temporal para la cámara.
     * La UI llamará a esta función ANTES de lanzar la cámara.
     */
    fun getTempImageUri(): Uri {
        val uri = fileProviderUtil.getTempImageUri()
        // Guarda temporalmente esta URI, ya que la cámara escribirá en ella
        _uiState.update { it.copy(profileImageUri = uri) }
        return uri
    }

    /**
     * Se llama cuando la galería devuelve una URI.
     */
    fun onImageSelectedFromGallery(uri: Uri) {
        _uiState.update { it.copy(profileImageUri = uri) }
        // (En una app real, aquí subiríamos la imagen al servidor)
    }

    /**
     * Se llama cuando la cámara confirma que la foto fue tomada.
     * La URI ya debería estar en el uiState desde getTempImageUri().
     */
    fun onImageCaptured() {
        // La URI ya está en el estado. Solo confirmamos.
        // (En una app real, aquí subiríamos la imagen al servidor)
    }
    // --- FIN CAMBIO 2 ---

    private fun loadUserData() {
        viewModelScope.launch {
            val email = sessionDataStore.userEmailFlow.first()
            if (email == null) {
                _uiState.update { it.copy(isLoading = false) }
                _logoutSuccess.value = true
                return@launch
            }
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nombre = user.nombre,
                        email = user.email,
                        esUsuarioDuoc = user.isDuocUser
                        // (profileImageUri se cargará desde otra fuente, ej. DataStore o BD)
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionDataStore.clearSession()
            _logoutSuccess.value = true
        }
    }
}