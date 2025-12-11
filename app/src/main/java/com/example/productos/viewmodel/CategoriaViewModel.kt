package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productos.data.remote.CategoriaApiService
import com.example.productos.data.remote.RetrofitCliente
import com.example.productos.data.repository.CategoriaRepository
import com.example.productos.model.Categoria
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {

    private val api = RetrofitCliente.retrofit.create(CategoriaApiService::class.java)
    private val repository = CategoriaRepository(api)

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    fun cargarCategorias() {
        viewModelScope.launch {
            try {
                _categorias.value = repository.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
