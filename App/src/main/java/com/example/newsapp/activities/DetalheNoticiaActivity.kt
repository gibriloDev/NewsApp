package com.example.newsapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.databinding.ActivityDetalheNoticiaBinding
import com.example.newsapp.model.Constantes
import com.example.newsapp.model.Noticia
import kotlinx.coroutines.launch

/**
 * Activity de detalhe de uma notícia.
 *
 * PARTE DE: Gibrilo
 * Cobre: detalhe completo, barra de credibilidade por fontes,
 *        AlertDialog de confirmação ao eliminar, botão favorito,
 *        abrir notícia no browser
 */
class DetalheNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheNoticiaBinding
    private lateinit var database: AppDatabase
    private lateinit var noticia: Noticia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalheNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhe"

        // Receber notícia via Intent (@Parcelize)
        @Suppress("DEPRECATION")
        noticia = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constantes.EXTRA_NOTICIA, Noticia::class.java)!!
        } else {
            intent.getParcelableExtra(Constantes.EXTRA_NOTICIA)!!
        }

        database = AppDatabase.obterInstancia(this)

        preencherDados()

        // Botão editar
        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, AdicionarNoticiaActivity::class.java)
            intent.putExtra(Constantes.EXTRA_NOTICIA, noticia)
            intent.putExtra(Constantes.EXTRA_MODO_EDICAO, true)
            startActivity(intent)
            finish()
        }

        // Botão eliminar com AlertDialog de confirmação
        binding.btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Notícia")
                .setMessage("Tens a certeza que queres eliminar esta notícia?")
                .setPositiveButton("Eliminar") { _, _ ->
                    lifecycleScope.launch {
                        database.noticiaDao().eliminar(noticia)
                        Toast.makeText(this@DetalheNoticiaActivity, "Notícia eliminada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Botão favorito
        atualizarBotaoFavorito()
        binding.btnFavorita.setOnClickListener {
            lifecycleScope.launch {
                noticia = noticia.copy(favorita = !noticia.favorita)
                database.noticiaDao().atualizar(noticia)
                atualizarBotaoFavorito()
                val msg = if (noticia.favorita) "Adicionada aos favoritos" else "Removida dos favoritos"
                Toast.makeText(this@DetalheNoticiaActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Botão ler no browser
        binding.btnLerNoticia.setOnClickListener {
            if (noticia.urlNoticia.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticia.urlNoticia))
                startActivity(intent)
            } else {
                Toast.makeText(this, "URL da notícia não disponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Preenche todos os campos da UI com os dados da notícia.
     * Inclui a barra de credibilidade por número de fontes.
     */
    private fun preencherDados() {
        binding.tvTitulo.text = noticia.titulo
        binding.tvDescricao.text = noticia.descricao
        binding.tvFonte.text = "Fonte: ${noticia.fonte}"
        binding.tvCategoria.text = noticia.categoria
        binding.tvData.text = noticia.dataPublicacao

        // ─── Barra de credibilidade ───
        // Lógica: até 10 fontes máximo para a barra estar a 100%
        val maxFontes = 10
        val percentagem = ((noticia.totalFontes.coerceAtMost(maxFontes).toFloat() / maxFontes) * 100).toInt()

        binding.progressCredibilidade.progress = percentagem
        binding.tvCredibilidade.text = when {
            percentagem >= 80 -> "Alta credibilidade (${noticia.totalFontes} fontes)"
            percentagem >= 50 -> "Credibilidade média (${noticia.totalFontes} fontes)"
            else -> "Credibilidade baixa (${noticia.totalFontes} fonte${if (noticia.totalFontes == 1) "" else "s"})"
        }
    }

    private fun atualizarBotaoFavorito() {
        val icone = if (noticia.favorita)
            android.R.drawable.btn_star_big_on
        else
            android.R.drawable.btn_star_big_off
        binding.btnFavorita.setImageResource(icone)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
