package com.example.newsapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Entidade principal da app.
 * Representa uma notícia guardada localmente.
 *
 * @Parcelize — permite passar o objeto entre Activities via Intent
 * @Entity    — define a tabela Room "noticias"
 */
@Parcelize
@Entity(tableName = "noticias")
data class Noticia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,           // Título da notícia
    val descricao: String,        // Resumo / descrição
    val fonte: String,            // Nome da fonte (ex: "BBC", "Público")
    val urlImagem: String,        // URL da imagem de capa
    val urlNoticia: String,       // URL para ler a notícia completa
    val dataPublicacao: String,   // Data de publicação (formato "DD/MM/YYYY")
    val categoria: String,        // Ex: "Tecnologia", "Desporto", "Política"
    val totalFontes: Int = 1,     // Nº de fontes que publicaram esta notícia (indicador de credibilidade)
    val favorita: Boolean = false // Marcada como favorita pelo utilizador
) : Parcelable
