package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productos.data.UsuarioEntity
import com.example.productos.repository.RepositorioUsuarios
import com.example.productos.repository.RepositorioLogin
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val repoUsuarios = RepositorioUsuarios()
    private val repoLogin = RepositorioLogin()

    // ----------------------------------------
    // REGISTRO DE USUARIO
    // ----------------------------------------
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

    // ----------------------------------------
    // LOGIN DE USUARIO
    // ----------------------------------------
    fun login(
        email: String,
        clave: String,
        callback: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repoLogin.login(email, clave)

                // Si backend retorna un usuario → Login correcto
                val ok = response.isSuccessful && response.body() != null

                callback(ok)

            } catch (e: Exception) {
                callback(false)
            }
        }
    }
}
