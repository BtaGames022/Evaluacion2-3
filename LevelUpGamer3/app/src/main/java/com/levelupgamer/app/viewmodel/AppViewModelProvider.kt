package com.levelupgamer.app.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer       // <--- IMPORTANTE
import androidx.lifecycle.viewmodel.viewModelFactory // <--- IMPORTANTE
import com.levelupgamer.app.LevelUpGamerApplication
import com.levelupgamer.app.data.SpringRepositoryImpl

object AppViewModelProvider {

    val Factory = viewModelFactory {
        // Initializer para RegistrationViewModel
        initializer {
            RegistrationViewModel(
                levelupGamerApplication()
            )
        }

        // Initializer para LoginViewModel
        initializer {
            LoginViewModel(
                levelupGamerApplication()
            )
        }

        // Initializer para ProfileViewModel
        initializer {
            ProfileViewModel(
                levelupGamerApplication()
            )
        }

        // Initializer para HomeViewModel
        initializer {
            HomeViewModel(
                levelupGamerApplication()
            )
        }

        // Initializer para CartViewModel
        initializer {
            CartViewModel(
                levelupGamerApplication()
            )
        }

        // Initializer para PostViewModel (Blog)
        initializer {
            PostViewModel()
        }

        // Initializer para SpringViewModel (Microservicio)
        initializer {
            SpringViewModel(
                SpringRepositoryImpl()
            )
        }
    }
}

/**
 * Función de extensión para obtener la aplicación.
 */
fun CreationExtras.levelupGamerApplication(): LevelUpGamerApplication {
    val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
    if (application != null) {
        return (application as LevelUpGamerApplication)
    }
    throw IllegalStateException("LevelUpGamerApplication no encontrada en CreationExtras")
}