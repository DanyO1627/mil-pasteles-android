package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.productos.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsuarioViewModel : ViewModel() {

    //  lista reactiva de usuarios (permanentes + creados en esta ejecución)
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios = _usuarios.asStateFlow()

    init {
        //  cargamos los 15 usuarios permanentes cuando se crea el ViewModel
        _usuarios.value = usuariosBase
    }

    //  registrar un nuevo usuario "temporal" (solo vive mientras la app está abierta)
    fun registrarUsuario(
        nombre: String,
        correo: String,
        contraseña: String,
        region: String,
        comuna: String
    ): Boolean {
        // validaciones básicas mínimas (la pantalla hace otras más específicas)
        if (nombre.isBlank() || correo.isBlank() || contraseña.length < 6 ||
            region.isBlank() || comuna.isBlank()
        ) return false

        // evitar correos duplicados (incluye permanentes y temporales)
        if (_usuarios.value.any { it.correo.equals(correo, ignoreCase = true) }) {
            return false
        }

        val nuevo = Usuario(
            nombre = nombre,
            correo = correo,
            contraseña = contraseña,
            region = region,
            comuna = comuna,
            esPermanente = false // 👈 creado en runtime
        )

        _usuarios.value = _usuarios.value + nuevo
        return true
    }

    //  helper opcional para login
    fun validarCredenciales(correo: String, contraseña: String): Boolean {
        return _usuarios.value.any {
            it.correo.equals(correo, ignoreCase = true) && it.contraseña == contraseña
        }
    }

    // para debug / mostrar lista si alguna vez lo necesitas
    fun obtenerTodos(): List<Usuario> = _usuarios.value

    companion object {
        //  AQUÍ van los 15 usuarios permanentes que me pediste
        val usuariosBase = listOf(
            Usuario(
                nombre = "Ana Ramírez Soto",
                correo = "ana.ramirez@mail.com",
                contraseña = "123456",
                region = "Región Metropolitana",
                comuna = "Providencia",
                esPermanente = true
            ),
            Usuario(
                nombre = "Felipe Morales Díaz",
                correo = "felipe.morales@mail.com",
                contraseña = "123456",
                region = "Valparaíso",
                comuna = "Viña del Mar",
                esPermanente = true
            ),
            Usuario(
                nombre = "Camila Torres Pérez",
                correo = "camila.torres@mail.com",
                contraseña = "123456",
                region = "Biobío",
                comuna = "Concepción",
                esPermanente = true
            ),
            Usuario(
                nombre = "Ignacio León Herrera",
                correo = "ignacio.leon@mail.com",
                contraseña = "123456",
                region = "Coquimbo",
                comuna = "La Serena",
                esPermanente = true
            ),
            Usuario(
                nombre = "Daniela Castillo Fuentes",
                correo = "daniela.castillo@mail.com",
                contraseña = "123456",
                region = "Maule",
                comuna = "Talca",
                esPermanente = true
            ),
            Usuario(
                nombre = "José Martínez Campos",
                correo = "jose.martinez@mail.com",
                contraseña = "123456",
                region = "O'Higgins",
                comuna = "Rancagua",
                esPermanente = true
            ),
            Usuario(
                nombre = "Paula Vergara Silva",
                correo = "paula.vergara@mail.com",
                contraseña = "123456",
                region = "Los Lagos",
                comuna = "Puerto Montt",
                esPermanente = true
            ),
            Usuario(
                nombre = "Antonio Fuenzalida Rivera",
                correo = "antonio.fuenzalida@mail.com",
                contraseña = "123456",
                region = "Ñuble",
                comuna = "Chillán",
                esPermanente = true
            ),
            Usuario(
                nombre = "Francisca Rosas Figueroa",
                correo = "francisca.rosas@mail.com",
                contraseña = "123456",
                region = "Los Ríos",
                comuna = "Valdivia",
                esPermanente = true
            ),
            Usuario(
                nombre = "Rodrigo Palma Araya",
                correo = "rodrigo.palma@mail.com",
                contraseña = "123456",
                region = "Tarapacá",
                comuna = "Iquique",
                esPermanente = true
            ),
            Usuario(
                nombre = "Gabriela Salazar Muñoz",
                correo = "gabriela.salazar@mail.com",
                contraseña = "123456",
                region = "Antofagasta",
                comuna = "Antofagasta",
                esPermanente = true
            ),
            Usuario(
                nombre = "Cristian Vega Loyola",
                correo = "cristian.vega@mail.com",
                contraseña = "123456",
                region = "La Araucanía",
                comuna = "Temuco",
                esPermanente = true
            ),
            Usuario(
                nombre = "Patricia Calderón Cáceres",
                correo = "patricia.calderon@mail.com",
                contraseña = "123456",
                region = "Atacama",
                comuna = "Copiapó",
                esPermanente = true
            ),
            Usuario(
                nombre = "Matías Zamora Bravo",
                correo = "matias.zamora@mail.com",
                contraseña = "123456",
                region = "Arica y Parinacota",
                comuna = "Arica",
                esPermanente = true
            ),
            Usuario(
                nombre = "Sofía Alvarado Pizarro",
                correo = "sofia.alvarado@mail.com",
                contraseña = "123456",
                region = "Magallanes",
                comuna = "Punta Arenas",
                esPermanente = true
            )
        )
    }
}
