package com.example.productos.repository

import com.example.productos.data.UsuarioEntity
import com.example.productos.remote.RetrofitUsuario
import retrofit2.Response
import okhttp3.ResponseBody

class RepositorioLogin {

    private val api = RetrofitUsuario.apiUsuarios

    suspend fun login(email: String, clave: String): Response<UsuarioEntity> {

        val response = api.obtenerUsuarioPorEmail(email)

        if (response.isSuccessful) {
            val usuario = response.body()

            if (usuario != null && usuario.clave == clave) {
                // ✅ Login correcto
                return response
            }
        }

        // ❌ Login incorrecto
        return Response.error(
            401,
            ResponseBody.create(null, "Credenciales incorrectas")
        )
    }
}
