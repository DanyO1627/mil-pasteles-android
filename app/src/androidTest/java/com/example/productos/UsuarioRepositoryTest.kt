package com.example.productos

import com.example.productos.repository.RepositorioUsuarios
import org.junit.Assert.assertNotNull
import org.junit.Test

class UsuarioRepositoryTest {

    @Test
    fun repositorio_se_crea_correctamente() {
        val repo = RepositorioUsuarios()
        assertNotNull(repo)
    }
}
