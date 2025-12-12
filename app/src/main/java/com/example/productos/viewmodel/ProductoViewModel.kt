package com.example.productos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.productos.R
import com.example.productos.data.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale

// guarda productos en memoria mientras la app esté abierta

class ProductoViewModel : ViewModel() {


    fun formatearPrecio(valor: Int): String {
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        formato.minimumFractionDigits = 0
        return formato.format(valor)
    }

    // Lista interna de productos simulando una base de datos
    private val _listaProductos = MutableStateFlow<List<Producto>>(emptyList())
    val listaProductos = _listaProductos.asStateFlow()


    // Producto seleccionado (para la pantalla de detalle)
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado = _productoSeleccionado.asStateFlow()

    init {
        // Cargar productos simulados (los “iniciales” del sistema)
        _listaProductos.value = listOf(
            Producto(
                id = 1,
                nombre = "Torta Selva Negra",
                precio = 12000,
                imagen = R.drawable.torta_selva_negra,
                descripcion = "Bizcocho de chocolate con crema batida y cerezas.",
                descripcionLarga = "Capas húmedas de chocolate con crema fresca y cerezas ácidas.",
                categoriaId = 1, // Tortas
                stock = 5
            ),
            Producto(
                id = 2,
                nombre = "Pie de Limón",
                precio = 10000,
                imagen = R.drawable.pie_limon,
                descripcion = "Base crocante con relleno de limón y merengue dorado.",
                descripcionLarga = "Clásico postre casero con equilibrio entre acidez y dulzor.",
                categoriaId = 2, // Pies y Tartas
                stock = 8
            ),
            Producto(
                id = 3,
                nombre = "Cheesecake Frutos Rojos",
                precio = 10000,
                imagen = R.drawable.cheesecake,
                descripcion = "Cheesecake con base de galleta y cobertura de frutos rojos.",
                descripcionLarga = "Suave mezcla de queso crema con frutas ácidas y base crocante.",
                categoriaId = 2, // Pies y Tartas
                stock = 6
            ),
            Producto(
                id = 4,
                nombre = "Brownie XL",
                precio = 7990,
                imagen = R.drawable.brownie,
                descripcion = "Brownie de chocolate intenso con trozos de nuez.",
                descripcionLarga = "Postre húmedo, denso y extra chocolatoso.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 4
            ),

            // --- Nuevos Productos Agregados (IDs 5-12) ---
            Producto(
                id = 5,
                nombre = "Torta Tres Leches",
                precio = 11500,
                imagen = R.drawable.torta_tres_leches,
                descripcion = "Esponjoso bizcocho empapado en tres tipos de leche.",
                descripcionLarga = "Un postre clásico y muy húmedo, con una textura inigualable y un toque de crema batida.",
                categoriaId = 1, // Tortas
                stock = 7
            ),
            Producto(
                id = 6,
                nombre = "Torta Mil Hojas",
                precio = 13000,
                imagen = R.drawable.torta_mil_hojas,
                descripcion = "Delicadas capas de hojaldre con crema pastelera.",
                descripcionLarga = "Un clásico de la repostería francesa, crujiente y cremoso al mismo tiempo.",
                categoriaId = 1, // Tortas
                stock = 3
            ),
            Producto(
                id = 7,
                nombre = "Tarta de Frambuesa",
                precio = 10500,
                imagen = R.drawable.tarta_frambuesa,
                descripcion = "Base de masa quebrada con crema y frescas frambuesas.",
                descripcionLarga = "El equilibrio perfecto entre lo dulce y lo ácido, con una base crocante.",
                categoriaId = 2, // Pies y Tartas
                stock = 9
            ),
            Producto(
                id = 8,
                nombre = "Tarta de Manzana Clásica",
                precio = 9800,
                imagen = R.drawable.tarta_manzana,
                descripcion = "Masa hojaldrada con manzanas caramelizadas y canela.",
                descripcionLarga = "El reconfortante sabor de un postre casero, perfecto para cualquier ocasión.",
                categoriaId = 2, // Pies y Tartas
                stock = 12
            ),
            Producto(
                id = 9,
                nombre = "Pack de 6 Cupcakes",
                precio = 8500,
                imagen = R.drawable.cupcakes,
                descripcion = "Pack surtido de cupcakes: chocolate, vainilla y red velvet.",
                descripcionLarga = "Ideal para compartir o para deleitarse con una variedad de sabores.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 15
            ),
            Producto(
                id = 10,
                nombre = "Galletas con Chispas de Chocolate (Sin Gluten)",
                precio = 5500,
                imagen = R.drawable.galletas,
                descripcion = "Deliciosas galletas caseras libres de gluten.",
                descripcionLarga = "Crujientes por fuera, suaves por dentro y llenas de chispas de chocolate. Una opción para todos.",
                categoriaId = 5, // Sin Gluten
                stock = 20
            ),
            Producto(
                id = 11,
                nombre = "Pastel de Vainilla y Fresas",
                precio = 11000,
                imagen = R.drawable.torta_crema_frutilla,
                descripcion = "Bizcocho esponjoso de vainilla con crema y fresas frescas.",
                descripcionLarga = "Un clásico que nunca falla, ligero y fresco, cubierto con las mejores fresas de temporada.",
                categoriaId = 1, // Tortas
                stock = 8
            ),
            Producto(
                id = 12,
                nombre = "Mousse de Maracuyá",
                precio = 7500,
                imagen = R.drawable.mousse_maracuya,
                descripcion = "Postre frío y ligero con el intenso sabor del maracuyá.",
                descripcionLarga = "Una textura aireada y un sabor exótico que refresca el paladar. Perfecto para climas cálidos.",
                categoriaId = 4, // Postres Fríos
                stock = 10
            ),
            Producto(
                id = 13,
                nombre = "Torta Red Velvet",
                precio = 13500,
                imagen = R.drawable.torta_red_velvet,
                descripcion = "Bizcocho rojo de vainilla y cacao con crema de queso.",
                descripcionLarga = "Suave y húmeda torta con un ligero toque a chocolate, cubierta con una cremosa frosting de queso crema.",
                categoriaId = 1, // Tortas
                stock = 8
            ),
            Producto(
                id = 14,
                nombre = "Torta de Zanahoria y Nueces",
                precio = 11800,
                imagen = R.drawable.torta_zanahoria,
                descripcion = "Esponjosa torta con zanahoria rallada, nueces y especias.",
                descripcionLarga = "Un clásico reconfortante, con un sabor dulce y especiado, cubierta con un delicioso glaseado de queso crema.",
                categoriaId = 1, // Tortas
                stock = 10
            ),
            Producto(
                id = 15,
                nombre = "Torta Opera",
                precio = 15000,
                imagen = R.drawable.torta_opera,
                descripcion = "Elegante torta francesa de café y chocolate.",
                descripcionLarga = "Fina capas de bizcocho de almendras empapado en café, con ganache de chocolate y crema de mantequilla de café.",
                categoriaId = 1, // Tortas
                stock = 4
            ),
            Producto(
                id = 16,
                nombre = "Torta Chajá",
                precio = 12500,
                imagen = R.drawable.torta_chaja,
                descripcion = "Bizcocho suave con dulce de leche, crema y duraznos.",
                descripcionLarga = "Un clásico uruguayo. Capas de bizcocho, dulce de leche, crema batida y trozos de durazno en almíbar.",
                categoriaId = 1, // Tortas
                stock = 9
            ),
            Producto(
                id = 17,
                nombre = "Torta Sacher",
                precio = 14000,
                imagen = R.drawable.torta_sacher,
                descripcion = "Bizcocho de chocolate con relleno de albaricoque y cobertura de chocolate.",
                descripcionLarga = "La famosa torta vienesa, una combinación perfecta de chocolate intenso y la acidez del albaricoque.",
                categoriaId = 1, // Tortas
                stock = 6
            ),
            Producto(
                id = 18,
                nombre = "Torta de Chocolate y Naranja",
                precio = 13000,
                imagen = R.drawable.torta_chocolate_naranja,
                descripcion = "Intenso bizcocho de chocolate con un toque cítrico de naranja.",
                descripcionLarga = "La combinación clásica que enamora. Un chocolate profundo realzado por la frescura de la naranja confitada.",
                categoriaId = 1, // Tortas
                stock = 7
            ),
            Producto(
                id = 19,
                nombre = "Pie de Key Lime",
                precio = 10500,
                imagen = R.drawable.pie_key_lime,
                descripcion = "Tarta de lima con una base de galleta y merengue tostado.",
                descripcionLarga = "Postre refrescante y ácido, originario de Florida, con un intenso sabor a lima.",
                categoriaId = 2, // Pies y Tartas
                stock = 11
            ),
            Producto(
                id = 20,
                nombre = "Apple Crumble",
                precio = 9500,
                imagen = R.drawable.apple_crumble,
                descripcion = "Manzanas cocidas con canela y una cobertura de avena crujiente.",
                descripcionLarga = "Clásico inglés casero. Caliente, reconfortante y delicioso, perfecto con una bola de helado.",
                categoriaId = 2, // Pies y Tartas
                stock = 13
            ),
            Producto(
                id = 21,
                nombre = "Pecan Pie",
                precio = 11000,
                imagen = R.drawable.pecan_pie,
                descripcion = "Tarta dulce de nuez pecan con un relleno de caramelo y jarabe de maíz.",
                descripcionLarga = "Un postre estadounidense denso, dulce y lleno de textura, irresistible para los amantes de los frutos secos.",
                categoriaId = 2, // Pies y Tartas
                stock = 8
            ),
            Producto(
                id = 22,
                nombre = "Tarta de Ricotta y Frutos Rojos",
                precio = 10800,
                imagen = R.drawable.tarta_ricotta,
                descripcion = "Crema de ricotta suave con una vibrante capa de frutos rojos.",
                descripcionLarga = "Un postre italiano ligero y cremoso, con el equilibrio perfecto entre el dulce de la ricotta y la acidez de las frutas.",
                categoriaId = 2, // Pies y Tartas
                stock = 9
            ),
            Producto(
                id = 23,
                nombre = "Quiche Lorraine",
                precio = 9900,
                imagen = R.drawable.quiche_lorraine,
                descripcion = "Tarta salada con bacon, queso y crema de leche.",
                descripcionLarga = "Un clásico francés para cualquier hora del día. Base crujiente con un relleno sabroso y cremoso.",
                categoriaId = 2, // Pies y Tartas
                stock = 5
            ),
            Producto(
                id = 24,
                nombre = "Macarons Surtidos",
                precio = 8900,
                imagen = R.drawable.macarons_surtidos,
                descripcion = "Pack de 12 macarons de sabores variados: frambuesa, pistacho, chocolate y lavanda.",
                descripcionLarga = "Deliciosos y delicados bocaditos franceses con una cáscara crujiente y un centro suave.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 20
            ),
            Producto(
                id = 25,
                nombre = "Alfajores de Maicena",
                precio = 6000,
                imagen = R.drawable.alfajores_maicena,
                descripcion = "Pack de 6 alfajores tiernos rellenos de dulce de leche.",
                descripcionLarga = "Clásicos argentinos, suaves y desmoronables, cubiertos con coco rallado o sin glasear.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 25
            ),
            Producto(
                id = 26,
                nombre = "Muffin de Arándanos",
                precio = 3500,
                imagen = R.drawable.muffin_arandanos,
                descripcion = "Muffin esponjoso con jugosos arándanos frescos.",
                descripcionLarga = "Un clásico de cafetería, perfecto para el desayuno o la once. Dulce, ácido y muy tierno.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 18
            ),
            Producto(
                id = 27,
                nombre = "Donas Glaseadas Surtidas",
                precio = 7500,
                imagen = R.drawable.donas_glaseadas,
                descripcion = "Pack de 4 donas con glaseados de colores y sabores.",
                descripcionLarga = "Esponjosas donas fritas cubiertas con nuestros deliciosos glaseados: vainilla, chocolate, fresa y caramelo.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 15
            ),
            Producto(
                id = 28,
                nombre = "Brownie de Nutella",
                precio = 4500,
                imagen = R.drawable.brownie_nutella,
                descripcion = "Denso brownie de chocolate con un corazón de Nutella.",
                descripcionLarga = "Para los amantes del chocolate. Un brownie increíblemente húmedo con un sorpresa cremosa de avellanas.",
                categoriaId = 3, // Cupcakes y Mini Delicias
                stock = 12
            ),
            Producto(
                id = 29,
                nombre = "Tiramisú Clásico",
                precio = 8000,
                imagen = R.drawable.tiramisu,
                descripcion = "Capas de bizcochos de soha empapados en café con mascarpone.",
                descripcionLarga = "El icónico postre italiano. Una combinación de café fuerte, queso mascarpone y un toque de cacao.",
                categoriaId = 4, // Postres Fríos
                stock = 10
            ),
            Producto(
                id = 30,
                nombre = "Panna Cotta de Vainilla",
                precio = 6500,
                imagen = R.drawable.panna_cotta_vainilla,
                descripcion = "Postre italiano de crema cuajada con salsa de frutos rojos.",
                descripcionLarga = "Textura sedosa y suave, con el delicado sabor de la vainilla y una salsa ácida y fresca.",
                categoriaId = 4, // Postres Fríos
                stock = 14
            ),
            Producto(
                id = 31,
                nombre = "Flan de Huevo Tradicional",
                precio = 5500,
                imagen = R.drawable.flan_huevo,
                descripcion = "Clásico flan casero con su caramelo líquido.",
                descripcionLarga = "El postre de la abuela. Suave, jugoso y con ese inconfundible sabor a caramelo quemado.",
                categoriaId = 4, // Postres Fríos
                stock = 16
            ),
            Producto(
                id = 32,
                nombre = "Arroz con Leche y Canela",
                precio = 5000,
                imagen = R.drawable.arroz_con_leche,
                descripcion = "Crema de arroz cocida en leche con azúcar y canela.",
                descripcionLarga = "Un postre reconfortante y aromático, servido frío o a temperatura ambiente con un poco de canela en polvo por encima.",
                categoriaId = 4, // Postres Fríos
                stock = 12
            ),
            Producto(
                id = 33,
                nombre = "Sorbete de Limón y Menta",
                precio = 4500,
                imagen = R.drawable.sorbete_limon_menta,
                descripcion = "Postre helado y refrescante, sin grasa.",
                descripcionLarga = "Ideal para limpiar el paladar. Intenso sabor a limón fresco con un toque herbáceo de menta.",
                categoriaId = 4, // Postres Fríos
                stock = 20
            ),
            Producto(
                id = 34,
                nombre = "Torta Chocolate Intenso (Sin Gluten)",
                precio = 14500,
                imagen = R.drawable.torta_chocolate_sin_gluten,
                descripcion = "Exuberante torta de chocolate sin harina de trigo.",
                descripcionLarga = "Apta para celíacos. Una experiencia de chocolate profunda y húmeda, para los más exigentes.",
                categoriaId = 5, // Sin Gluten
                stock = 5
            ),
            Producto(
                id = 35,
                nombre = "Cheesecake de Frutos Rojos (Sin Gluten)",
                precio = 11500,
                imagen = R.drawable.cheesecake_frutos_rojos_sg,
                descripcion = "Clásico cheesecake con base de galleta sin gluten.",
                descripcionLarga = "La misma cremosidad y sabor delicioso, pero elaborado con ingredientes seguros para una dieta sin gluten.",
                categoriaId = 5, // Sin Gluten
                stock = 7
            ),
            Producto(
                id = 36,
                nombre = "Brownie de Nueces (Sin Gluten)",
                precio = 5500,
                imagen = R.drawable.brownie_nueces_sg,
                descripcion = "Denso y húmedo brownie de chocolate sin gluten.",
                descripcionLarga = "Ningún sacrificio en el sabor. Un brownie perfecto, crocante por fuera y tierno por dentro, sin rastro de gluten.",
                categoriaId = 5, // Sin Gluten
                stock = 10
            ),
            Producto(
                id = 37,
                nombre = "Mousse de Chocolate Blanco (Sin Gluten)",
                precio = 7000,
                imagen = R.drawable.mousse_chocolate_blanco_sg,
                descripcion = "Aireado y dulce mousse de chocolate blanco naturalmente sin gluten.",
                descripcionLarga = "Una nube de sabor. Ligero, cremoso y con la delicadeza del mejor chocolate blanco.",
                categoriaId = 5, // Sin Gluten
                stock = 9
            ),
            Producto(
                id = 38,
                nombre = "Tarta de Almendras y Frambuesa (Sin Gluten)",
                precio = 12000,
                imagen = R.drawable.tarta_almendras_frambuesa_sg,
                descripcion = "Base de almendra con frambuesas frescas y un toque de miel.",
                descripcionLarga = "Elegante y deliciosa opción sin gluten. La textura de la almendra combina perfectamente con la acidez de la frambuesa.",
                categoriaId = 5, // Sin Gluten
                stock = 6
            ),
            Producto(
                id = 39,
                nombre = "Torta Zanahoria (Sin Gluten)",
                precio = 13000,
                imagen = R.drawable.torta_zanahoria_sg,
                descripcion = "La clásica torta de zanahoria y nueces, versión sin gluten.",
                descripcionLarga = "Húmeda, especiada y deliciosa. Nadie notará la diferencia en esta versión apta para celíacos.",
                categoriaId = 5, // Sin Gluten
                stock = 4
            ),
            Producto(
                id = 40,
                nombre = "Galletas de Avena y Pasas (Sin Gluten)",
                precio = 5000,
                imagen = R.drawable.galletas_avena_pasas_sg,
                descripcion = "Pack de galletas caseras de avena sin gluten.",
                descripcionLarga = "Una opción saludable y deliciosa. Crujientes, con un toque dulce de las pasas y la textura de la avena.",
                categoriaId = 5, // Sin Gluten
                stock = 22
            ),
            Producto(
                id = 41,
                nombre = "Torta Mil Hojas (Sin Gluten)",
                precio = 15500,
                imagen = R.drawable.torta_mil_hojas_sg,
                descripcion = "Delicado hojaldre sin gluten con crema pastelera.",
                descripcionLarga = "Un desafío técnico logrado. Todas las capas crujientes y cremosidad del clásico, en una versión segura para todos.",
                categoriaId = 5, // Sin Gluten
                stock = 3
            ),
            Producto(
                id = 42,
                nombre = "Cupcakes de Vainilla (Sin Gluten)",
                precio = 4000,
                imagen = R.drawable.cupcakes_vainilla_sg,
                descripcion = "Pack de 4 cupcakes de vainilla con frosting de crema de queso.",
                descripcionLarga = "Pequeños y deliciosos bocaditos sin gluten. Esponjosos y con un frosting que te encantará.",
                categoriaId = 5, // Sin Gluten
                stock = 18
            )


        )
    }

    fun productosHome(): List<Producto> {
        val ids = listOf(15, 13, 1, 24, 17, 16, 2, 27, 3, 34)
        return listaProductos.value.filter { it.id in ids }
    }

    // selecciona producto por id (para detalle producto)
    fun seleccionarProducto(id: Int) {
        _productoSeleccionado.value = _listaProductos.value.find { it.id == id }
    }

    // actualizar producto existente
    fun actualizarProducto(producto: Producto) {
        _listaProductos.value = _listaProductos.value.map {
            if (it.id == producto.id) producto else it
        }
    }



    fun aumentarStock(productoId: Int, cantidad: Int = 1) {
        val lista = _listaProductos.value.toMutableList()
        val i = lista.indexOfFirst { it.id == productoId }
        if (i != -1) {
            val p = lista[i]
            lista[i] = p.copy(stock = p.stock + cantidad)
            _listaProductos.value = lista
        }
    }

    fun obtenerProductoPorId(id: Int) = _listaProductos.value.find { it.id == id }



    // disminuye stock cuando se compra
    fun disminuirStock(productoId: Int, cantidad: Int) {
        val listaActual = _listaProductos.value.toMutableList()
        val index = listaActual.indexOfFirst { it.id == productoId }
        if (index != -1) {
            val producto = listaActual[index]
            if (producto.stock >= cantidad) {
                listaActual[index] = producto.copy(stock = producto.stock - cantidad)
                _listaProductos.value = listaActual
            }
        }
    }

    fun obtenerProductosPorCategoria(nombreCategoria: String): List<Producto> {
        return when (nombreCategoria) {
            "Tortas" -> _listaProductos.value.filter { it.categoriaId == 1 }
            "Pies y Tartas" -> _listaProductos.value.filter { it.categoriaId == 2 }
            "Cupcakes y Mini Delicias" -> _listaProductos.value.filter { it.categoriaId == 3 }
            "Postres Fríos" -> _listaProductos.value.filter { it.categoriaId == 4 }
            "Sin Gluten" -> _listaProductos.value.filter { it.categoriaId == 5 }
            else -> emptyList()
        }
    }


}