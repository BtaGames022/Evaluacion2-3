package com.levelupgamer.app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory (fábrica) para crear el ProductDetailViewModel,
 * ya que necesita parámetros (productId).
 */
class ProductDetailViewModelFactory(
    private val application: Application,
    private val productId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(application, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}