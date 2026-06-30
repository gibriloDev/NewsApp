package com.example.newsapp.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton que cria e fornece a instância do Retrofit.
 * URL base da NewsAPI: https://newsapi.org/
 */
object RetrofitCliente {

    private const val BASE_URL = "https://newsapi.org/"

    // Logging de rede para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP com timeouts
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Instância Retrofit (criada uma única vez)
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    // Serviço pronto a usar
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
