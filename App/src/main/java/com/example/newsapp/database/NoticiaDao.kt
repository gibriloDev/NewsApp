package com.example.newsapp.database

import androidx.room.*
import com.example.newsapp.model.Noticia
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para a tabela "noticias".
 * Define todas as operações CRUD usando Room.
 */
@Dao
interface NoticiaDao {

    // Inserir notícia (substitui se já existir)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(noticia: Noticia)

    // Atualizar notícia existente
    @Update
    suspend fun atualizar(noticia: Noticia)

    // Eliminar notícia
    @Delete
    suspend fun eliminar(noticia: Noticia)

    // Obter todas as notícias ordenadas por data (mais recente primeiro)
    @Query("SELECT * FROM noticias ORDER BY dataPublicacao DESC")
    fun obterTodas(): Flow<List<Noticia>>

    // Obter notícia por ID
    @Query("SELECT * FROM noticias WHERE id = :id")
    suspend fun obterPorId(id: Int): Noticia?

    // Pesquisar por título ou fonte (pesquisa em tempo real)
    @Query("SELECT * FROM noticias WHERE titulo LIKE '%' || :texto || '%' OR fonte LIKE '%' || :texto || '%' ORDER BY dataPublicacao DESC")
    fun pesquisar(texto: String): Flow<List<Noticia>>

    // Filtrar por categoria
    @Query("SELECT * FROM noticias WHERE categoria = :categoria ORDER BY dataPublicacao DESC")
    fun obterPorCategoria(categoria: String): Flow<List<Noticia>>

    // Obter apenas favoritas
    @Query("SELECT * FROM noticias WHERE favorita = 1 ORDER BY dataPublicacao DESC")
    fun obterFavoritas(): Flow<List<Noticia>>

    // Ordenar por título A-Z
    @Query("SELECT * FROM noticias ORDER BY titulo ASC")
    fun obterOrdenadasPorTitulo(): Flow<List<Noticia>>

    // Ordenar por nº de fontes (mais credíveis primeiro)
    @Query("SELECT * FROM noticias ORDER BY totalFontes DESC")
    fun obterOrdenadasPorFontes(): Flow<List<Noticia>>

    // Eliminar todas (usado ao recarregar da API)
    @Query("DELETE FROM noticias")
    suspend fun eliminarTodas()

    // Contar notícias existentes
    @Query("SELECT COUNT(*) FROM noticias")
    suspend fun contar(): Int
}
