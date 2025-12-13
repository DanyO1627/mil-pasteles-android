package com.example.productos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productos.data.remote.ProductoApiService
import com.example.productos.data.remote.RetrofitCliente
import com.example.productos.data.repository.ProductoRepository
import com.example.productos.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val api = RetrofitCliente.retrofit.create(ProductoApiService::class.java)
    private val repository = ProductoRepository(api)

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos
    val listaProductos = productos

    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado

    init {
        Log.d("ProductoViewModel", "🔵 ViewModel inicializado")
        Log.d("ProductoViewModel", "🔵 BASE_URL configurada: http://10.0.2.2:8011/api/")
    }

    fun cargarProductos() {
        Log.d("ProductoViewModel", "🔵 ========================================")
        Log.d("ProductoViewModel", "🔵 INICIANDO cargarProductos()")
        Log.d("ProductoViewModel", "🔵 ========================================")

        viewModelScope.launch {
            try {
                Log.d("ProductoViewModel", "🔵 Llamando al repository.getAll()...")
                val resultado = repository.getAll()

                Log.d("ProductoViewModel", "✅ ========================================")
                Log.d("ProductoViewModel", "✅ ÉXITO: Productos recibidos: ${resultado.size}")
                Log.d("ProductoViewModel", "✅ ========================================")

                // Mostrar los primeros 3 productos
                resultado.take(3).forEachIndexed { index, producto ->
                    Log.d("ProductoViewModel", "  ${index + 1}. ${producto.nombreProducto} - Precio: ${producto.precio} - Stock: ${producto.stock}")
                }

                _productos.value = resultado
                Log.d("ProductoViewModel", "✅ StateFlow actualizado correctamente")

            } catch (e: Exception) {
                Log.e("ProductoViewModel", "❌ ========================================")
                Log.e("ProductoViewModel", "❌ ERROR AL CARGAR PRODUCTOS")
                Log.e("ProductoViewModel", "❌ ========================================")
                Log.e("ProductoViewModel", "❌ Tipo de error: ${e.javaClass.simpleName}")
                Log.e("ProductoViewModel", "❌ Mensaje: ${e.message}")
                Log.e("ProductoViewModel", "❌ ========================================")

                e.printStackTrace()

                // Diagnóstico específico
                when (e) {
                    is java.net.UnknownServiceException -> {
                        Log.e("ProductoViewModel", "❌ CLEARTEXT no permitido")
                        Log.e("ProductoViewModel", "❌ Verifica network_security_config.xml")
                        Log.e("ProductoViewModel", "❌ Hiciste Clean + Rebuild?")
                    }
                    is java.net.ConnectException -> {
                        Log.e("ProductoViewModel", "❌ No se puede conectar al servidor")
                        Log.e("ProductoViewModel", "❌ Backend corriendo en puerto 8011?")
                    }
                    is java.net.SocketTimeoutException -> {
                        Log.e("ProductoViewModel", "❌ Timeout - servidor no responde")
                    }
                    is retrofit2.HttpException -> {
                        Log.e("ProductoViewModel", "❌ Error HTTP: ${e.code()}")
                        Log.e("ProductoViewModel", "❌ URL: ${e.response()?.raw()?.request?.url}")
                    }
                }
            }
        }
    }

    fun seleccionarProducto(id: Long?) {
        _productoSeleccionado.value = obtenerProductoPorId(id)
    }

    fun obtenerProductoPorId(id: Long?): Producto? {
        return productos.value.find { it.id == id }
    }

    fun disminuirStock(id: Long?, cantidad: Int) {
        val lista = productos.value.toMutableList()
        val index = lista.indexOfFirst { it.id == id }
        if (index != -1) {
            val p = lista[index]
            lista[index] = p.copy(stock = p.stock - cantidad)
            _productos.value = lista
        }
    }

    fun aumentarStock(id: Long?, cantidad: Int) {
        val lista = productos.value.toMutableList()
        val index = lista.indexOfFirst { it.id == id }
        if (index != -1) {
            val p = lista[index]
            lista[index] = p.copy(stock = p.stock + cantidad)
            _productos.value = lista
        }
    }
}