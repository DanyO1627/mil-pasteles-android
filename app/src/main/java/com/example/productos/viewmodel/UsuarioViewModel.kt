package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productos.data.UsuarioEntity
import com.example.productos.repository.RepositorioUsuarios
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val repoUsuarios = RepositorioUsuarios()

    // LOGIN
    fun login(
        email: String,
        claveIngresada: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repoUsuarios.obtenerUsuarioPorEmail(email)

                if (response.isSuccessful) {
                    val usuario = response.body()
                    val loginValido = usuario != null && usuario.clave == claveIngresada
                    callback(loginValido)
                } else {
                    callback(false)
                }

            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    // REGISTRO
    fun registrarUsuario(
        nombre: String,
        email: String,
        clave: String,
        region: String,
        comuna: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usuario = UsuarioEntity(
                    id = null,
                    nombre = nombre,
                    email = email,
                    clave = clave,
                    region = region,
                    comuna = comuna
                )

                val response = repoUsuarios.registrarUsuario(usuario)
                callback(response.isSuccessful)

            } catch (e: Exception) {
                callback(false)
            }
        }
    }
}