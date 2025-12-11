package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CarritoViewModelFactory(
    private val productoViewModel: ProductoViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(productoViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}