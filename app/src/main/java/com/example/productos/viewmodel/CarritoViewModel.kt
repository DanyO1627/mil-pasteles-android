package com.example.productos.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.productos.data.CarritoItem
import com.example.productos.remote.RetrofitInstance
import com.example.productos.repository.CarritoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CarritoViewModelFactory(
    private val repository: CarritoRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "carrito_prefs")

class CarritoViewModel(
    private val repository: CarritoRepository,
    private val context: Context
) : ViewModel() {

    // Key para guardar el ID del carrito en DataStore
    private val CARRITO_ID_KEY = longPreferencesKey("carrito_id")

    // ========================================
    // ESTADOS OBSERVABLES
    // ========================================

    private val _listaCarrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val listaCarrito = _listaCarrito.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total = _total.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // ID del carrito actual
    private var carritoId: Long? = null

    init {
        // Al iniciar, intentar cargar carrito existente
        viewModelScope.launch {
            cargarCarritoExistente()
        }
    }

    // ========================================
    // FUNCIONES PRIVADAS DE PERSISTENCIA
    // ========================================

    private suspend fun guardarCarritoId(id: Long) {
        context.dataStore.edit { preferences ->
            preferences[CARRITO_ID_KEY] = id
        }
        carritoId = id
    }

    private suspend fun obtenerCarritoId(): Long? {
        val preferences = context.dataStore.data.first()
        return preferences[CARRITO_ID_KEY]
    }

    private suspend fun borrarCarritoId() {
        context.dataStore.edit { preferences ->
            preferences.remove(CARRITO_ID_KEY)
        }
        carritoId = null
    }

    // ========================================
    // FUNCIONES PÚBLICAS DE CARRITO
    // ========================================

    private suspend fun cargarCarritoExistente() {
        _isLoading.value = true
        val id = obtenerCarritoId()

        if (id != null) {
            repository.obtenerCarrito(id).fold(
                onSuccess = { response ->
                    carritoId = response.carritoId
                    actualizarEstadoDesdeResponse(response)
                    _error.value = null
                },
                onFailure = {
                    // Si falla, crear uno nuevo
                    crearNuevoCarrito()
                }
            )
        } else {
            // No hay carrito guardado, crear uno nuevo
            crearNuevoCarrito()
        }

        _isLoading.value = false
    }

    private suspend fun crearNuevoCarrito() {
        repository.crearCarrito().fold(
            onSuccess = { response ->
                guardarCarritoId(response.carritoId)
                actualizarEstadoDesdeResponse(response)
                _error.value = null
            },
            onFailure = { e ->
                _error.value = "Error al crear carrito: ${e.message}"
            }
        )
    }

    // ✅ AGREGAR PRODUCTO AL CARRITO
    fun agregarAlCarrito(productoId: Long, cantidad: Int = 1) {
        viewModelScope.launch {
            _isLoading.value = true

            // Asegurar que tenemos un carrito
            val id = carritoId ?: run {
                crearNuevoCarrito()
                carritoId
            }

            if (id != null) {
                repository.agregarItem(id, productoId, cantidad).fold(
                    onSuccess = { response ->
                        actualizarEstadoDesdeResponse(response)
                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = "Error al agregar producto: ${e.message}"
                    }
                )
            }

            _isLoading.value = false
        }
    }

    // ✅ AUMENTAR CANTIDAD (llama a agregarAlCarrito con cantidad 1)
    fun aumentarCantidad(item: CarritoItem) {
        agregarAlCarrito(item.productoId, 1)
    }

    // ✅ DISMINUIR CANTIDAD
    fun disminuirCantidad(item: CarritoItem) {
        viewModelScope.launch {
            _isLoading.value = true

            val id = carritoId
            if (id != null) {
                repository.disminuirItem(id, item.productoId).fold(
                    onSuccess = { response ->
                        actualizarEstadoDesdeResponse(response)
                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = "Error al disminuir cantidad: ${e.message}"
                    }
                )
            }

            _isLoading.value = false
        }
    }

    // ✅ ELIMINAR ITEM COMPLETO
    fun eliminar(item: CarritoItem) {
        viewModelScope.launch {
            _isLoading.value = true

            val id = carritoId
            if (id != null) {
                repository.eliminarItem(id, item.productoId).fold(
                    onSuccess = { response ->
                        actualizarEstadoDesdeResponse(response)
                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = "Error al eliminar producto: ${e.message}"
                    }
                )
            }

            _isLoading.value = false
        }
    }

    // ✅ VACIAR CARRITO COMPLETO
    fun vaciarCarrito() {
        viewModelScope.launch {
            _isLoading.value = true

            val id = carritoId
            if (id != null) {
                repository.vaciarCarrito(id).fold(
                    onSuccess = { response ->
                        actualizarEstadoDesdeResponse(response)
                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = "Error al vaciar carrito: ${e.message}"
                    }
                )
            }

            _isLoading.value = false
        }
    }

    // ✅ CONFIRMAR COMPRA
    fun confirmarCompra() {
        viewModelScope.launch {
            _isLoading.value = true

            val id = carritoId
            if (id != null) {
                repository.confirmarCompra(id).fold(
                    onSuccess = { response ->
                        // Limpiar estado local
                        _listaCarrito.value = emptyList()
                        _total.value = 0.0

                        // Borrar carrito guardado y crear uno nuevo
                        borrarCarritoId()
                        crearNuevoCarrito()

                        _error.value = null
                    },
                    onFailure = { e ->
                        _error.value = "Error al confirmar compra: ${e.message}"
                    }
                )
            }

            _isLoading.value = false
        }
    }

    // ========================================
    // HELPER PRIVADO
    // ========================================

    private fun actualizarEstadoDesdeResponse(
        response: com.example.productos.data.dto.CarritoResponseDto
    ) {
        _listaCarrito.value = response.items.map { itemDto ->
            CarritoItem(
                productoId = itemDto.productoId,
                nombre = itemDto.nombreProducto,
                precio = itemDto.precioUnitario,
                imagen = itemDto.imagenUrl ?: "", // URL de la imagen
                cantidad = itemDto.cantidad,
                imagenUrl = itemDto.imagenUrl
            )
        }
        _total.value = response.total
    }

    fun limpiarError() {
        _error.value = null
    }

    // ========================================
    // FUNCIÓN AUXILIAR PARA FORMATEAR PRECIO
    // (Para no depender de ProductoViewModel)
    // ========================================

    fun formatearPrecio(precio: Double): String {
        return "$${String.format("%,.0f", precio)}"
    }
}

// ========================================
// EJEMPLO DE USO EN ACTIVITY/FRAGMENT
// ========================================

/*
class MainActivity : ComponentActivity() {

    private val carritoRepository by lazy {
        CarritoRepository(RetrofitInstance.api)
    }

    private val carritoViewModel by viewModels<CarritoViewModel> {
        CarritoViewModelFactory(carritoRepository, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observar cambios
        lifecycleScope.launch {
            carritoViewModel.listaCarrito.collect { items ->
                // Actualizar UI con los items
            }
        }

        lifecycleScope.launch {
            carritoViewModel.total.collect { total ->
                // Actualizar UI con el total
            }
        }

        lifecycleScope.launch {
            carritoViewModel.error.collect { error ->
                error?.let {
                    // Mostrar error al usuario
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                    carritoViewModel.limpiarError()
                }
            }
        }
    }
}
*/