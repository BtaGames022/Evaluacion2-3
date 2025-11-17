package com.levelupgamer.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Importamos el modelo desde la ruta correcta
import com.levelupgamer.app.model.Post
// ¡Importante! Importamos el repositorio desde SU NUEVA RUTA
import com.levelupgamer.app.data.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado de la UI para la pantalla de Posts.
 */
data class PostUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * ViewModel para la pantalla de Posts (Guía 14)
 */
class PostViewModel : ViewModel() {

    // Instanciamos el repositorio desde su nueva ubicación
    private val repository = PostRepository()

    // Flujo mutable privado
    private val _uiState = MutableStateFlow(PostUiState())
    // Flujo público de solo lectura
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        fetchPosts()
    }

    /**
     * Obtiene los posts desde el repositorio en una corrutina
     */
    private fun fetchPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val posts = repository.getPosts()
                _uiState.update { it.copy(isLoading = false, posts = posts) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}