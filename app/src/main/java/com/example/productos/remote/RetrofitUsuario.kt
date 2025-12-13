package com.example.productos.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUsuario {

    private const val BASE_URL = "http://10.0.2.2:8011/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiUsuarios: ApiServiceUsuarios = retrofit.create(ApiServiceUsuarios::class.java)
}