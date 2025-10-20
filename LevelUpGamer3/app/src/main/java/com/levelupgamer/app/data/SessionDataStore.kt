package com.levelupgamer.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear la instancia de DataStore (basado en Guía 12)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

/**
 * Gestiona la sesión del usuario (ej. email logueado) usando DataStore Preferences.
 */
class SessionDataStore(private val context: Context) {

    companion object {
        // Clave para guardar el email
        val LOGGED_IN_USER_EMAIL = stringPreferencesKey("LOGGED_IN_USER_EMAIL")
    }

    /**
     * Guarda el email del usuario que acaba de iniciar sesión.
     */
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_USER_EMAIL] = email
        }
    }

    /**
     * Obtiene el email del usuario guardado como un Flow.
     * Si no hay email, devuelve null.
     */
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LOGGED_IN_USER_EMAIL]
        }

    /**
     * Borra la sesión del usuario (logout).
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(LOGGED_IN_USER_EMAIL)
        }
    }
}