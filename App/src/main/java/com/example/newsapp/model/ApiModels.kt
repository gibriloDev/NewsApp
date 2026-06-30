package com.example.newsapp.model

import com.google.gson.annotations.SerializedName

/**
 * Modelos de dados para fazer parse da resposta JSON da NewsAPI.
 * Documentação: https://newsapi.org/docs
 */

// Resposta raiz da API
data class RespostaApi(
    @SerializedName("status") val estado: String,         // "ok" ou "error"
    @SerializedName("totalResults") val totalResultados: Int,
    @SerializedName("articles") val artigos: List<ArtigoApi>
)

// Cada artigo devolvido pela API
data class ArtigoApi(
    @SerializedName("source") val fonte: FonteApi,
    @SerializedName("title") val titulo: String?,
    @SerializedName("description") val descricao: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlImagem: String?,
    @SerializedName("publishedAt") val dataPublicacao: String?  // formato ISO: "2026-06-15T10:00:00Z"
)

// Fonte do artigo
data class FonteApi(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val nome: String?
)
