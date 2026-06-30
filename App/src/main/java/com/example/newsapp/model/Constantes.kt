package com.example.newsapp.model

/**
 * Constantes globais da aplicação.
 */
object Constantes {

    // ───── API ─────
    // Regista-te em https://newsapi.org/register e substitui aqui
    const val API_KEY = "SUA_API_KEY_AQUI"

    // ───── SharedPreferences ─────
    const val PREFS_NOME = "newsapp_prefs"
    const val PREFS_MODO_ESCURO = "modo_escuro"
    const val PREFS_NOTIFICACOES = "notificacoes_ativas"
    const val PREFS_CATEGORIA_FILTRO = "categoria_filtro"

    // ───── Extras de Intent ─────
    const val EXTRA_NOTICIA = "extra_noticia"
    const val EXTRA_MODO_EDICAO = "extra_modo_edicao"

    // ───── Categorias ─────
    val CATEGORIAS = listOf(
        "Todas",
        "Tecnologia",
        "Desporto",
        "Política",
        "Saúde",
        "Ciência",
        "Entretenimento",
        "Negócios"
    )

    // Mapeamento categoria local → categoria API
    val CATEGORIAS_API = mapOf(
        "Tecnologia" to "technology",
        "Desporto" to "sports",
        "Saúde" to "health",
        "Ciência" to "science",
        "Entretenimento" to "entertainment",
        "Negócios" to "business"
    )

    // ───── Notificações ─────
    const val CANAL_NOTIFICACOES = "canal_noticias"
}
