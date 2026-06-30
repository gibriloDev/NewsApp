package com.example.newsapp.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface Retrofit que define os endpoints da NewsAPI.
 * Documentação: https://newsapi.org/docs/endpoints
 *
 * Regista-te em https://newsapi.org/register para obter uma API key gratuita.
 */
interface ApiService {

    /**
     * Obter notícias principais (top headlines).
     * Endpoint: GET /v2/top-headlines
     */
    @GET("v2/top-headlines")
    suspend fun obterTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("language") idioma: String = "pt",   // notícias em português
        @Query("pageSize") tamanho: Int = 20         // máximo 20 notícias por pedido
    ): Response<RespostaApi>

    /**
     * Pesquisar notícias por palavra-chave.
     * Endpoint: GET /v2/everything
     */
    @GET("v2/everything")
    suspend fun pesquisarNoticias(
        @Query("apiKey") apiKey: String,
        @Query("q") pesquisa: String,
        @Query("language") idioma: String = "pt",
        @Query("pageSize") tamanho: Int = 20,
        @Query("sortBy") ordenacao: String = "publishedAt"  // mais recentes primeiro
    ): Response<RespostaApi>

    /**
     * Obter notícias por categoria.
     * Categorias: business, entertainment, health, science, sports, technology
     */
    @GET("v2/top-headlines")
    suspend fun obterPorCategoria(
        @Query("apiKey") apiKey: String,
        @Query("category") categoria: String,
        @Query("language") idioma: String = "pt",
        @Query("pageSize") tamanho: Int = 20
    ): Response<RespostaApi>
}
