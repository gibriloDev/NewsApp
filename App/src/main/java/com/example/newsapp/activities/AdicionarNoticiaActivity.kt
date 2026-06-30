package com.example.newsapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.databinding.ActivityAdicionarNoticiaBinding
import com.example.newsapp.model.Constantes
import com.example.newsapp.model.Noticia
import com.example.newsapp.model.RetrofitCliente
import kotlinx.coroutines.launch

/**
 * Activity para adicionar notícia manualmente ou carregar da API.
 *
 * PARTE DE: Gibrilo
 * Cobre: formulário com validações, Retrofit (carregar da API),
 *        notificações locais quando há novas notícias
 */
class AdicionarNoticiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdicionarNoticiaBinding
    private lateinit var database: AppDatabase

    private var noticiaParaEditar: Noticia? = null
    private var modoEdicao: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdicionarNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.obterInstancia(this)

        // Verificar modo edição
        modoEdicao = intent.getBooleanExtra(Constantes.EXTRA_MODO_EDICAO, false)

        @Suppress("DEPRECATION")
        noticiaParaEditar = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constantes.EXTRA_NOTICIA, Noticia::class.java)
        } else {
            intent.getParcelableExtra(Constantes.EXTRA_NOTICIA)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (modoEdicao) "Editar Notícia" else "Adicionar Notícia"

        // Configurar Spinner de categorias
        val adapterCategorias = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constantes.CATEGORIAS.drop(1) // Remove "Todas"
        )
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapterCategorias

        // Preencher campos se for edição
        if (modoEdicao && noticiaParaEditar != null) {
            preencherCamposEdicao(noticiaParaEditar!!)
        }

        // Botão guardar manualmente
        binding.btnGuardar.setOnClickListener {
            if (validarFormulario()) {
                guardarNoticia()
            }
        }

        // Botão carregar da API (Retrofit)
        binding.btnCarregarApi.setOnClickListener {
            carregarDaApi()
        }
    }

    /**
     * Preenche os campos com os dados da notícia a editar.
     */
    private fun preencherCamposEdicao(noticia: Noticia) {
        binding.etTitulo.setText(noticia.titulo)
        binding.etDescricao.setText(noticia.descricao)
        binding.etFonte.setText(noticia.fonte)
        binding.etUrlNoticia.setText(noticia.urlNoticia)
        binding.etData.setText(noticia.dataPublicacao)
        binding.etTotalFontes.setText(noticia.totalFontes.toString())

        val indice = Constantes.CATEGORIAS.drop(1).indexOf(noticia.categoria)
        if (indice >= 0) binding.spinnerCategoria.setSelection(indice)
    }

    /**
     * Valida todos os campos do formulário.
     * Mostra mensagem de erro em cada campo inválido.
     */
    private fun validarFormulario(): Boolean {
        var valido = true

        // Título
        if (binding.etTitulo.text.toString().trim().isEmpty()) {
            binding.tilTitulo.error = "Introduz o título da notícia"
            valido = false
        } else {
            binding.tilTitulo.error = null
        }

        // Descrição
        if (binding.etDescricao.text.toString().trim().isEmpty()) {
            binding.tilDescricao.error = "Introduz uma descrição"
            valido = false
        } else {
            binding.tilDescricao.error = null
        }

        // Fonte
        if (binding.etFonte.text.toString().trim().isEmpty()) {
            binding.tilFonte.error = "Introduz o nome da fonte"
            valido = false
        } else {
            binding.tilFonte.error = null
        }

        // Data
        val data = binding.etData.text.toString().trim()
        if (data.isEmpty()) {
            binding.tilData.error = "Introduz a data (DD/MM/AAAA)"
            valido = false
        } else if (!data.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            binding.tilData.error = "Formato inválido. Usa DD/MM/AAAA"
            valido = false
        } else {
            binding.tilData.error = null
        }

        // Total de fontes
        val fontes = binding.etTotalFontes.text.toString().trim()
        if (fontes.isEmpty() || fontes.toIntOrNull() == null || fontes.toInt() < 1) {
            binding.tilTotalFontes.error = "Mínimo 1 fonte"
            valido = false
        } else {
            binding.tilTotalFontes.error = null
        }

        return valido
    }

    /**
     * Guarda a notícia na base de dados Room (criar ou atualizar).
     */
    private fun guardarNoticia() {
        val novaNoticia = Noticia(
            id = noticiaParaEditar?.id ?: 0,
            titulo = binding.etTitulo.text.toString().trim(),
            descricao = binding.etDescricao.text.toString().trim(),
            fonte = binding.etFonte.text.toString().trim(),
            urlImagem = "",
            urlNoticia = binding.etUrlNoticia.text.toString().trim(),
            dataPublicacao = binding.etData.text.toString().trim(),
            categoria = binding.spinnerCategoria.selectedItem.toString(),
            totalFontes = binding.etTotalFontes.text.toString().trim().toInt(),
            favorita = noticiaParaEditar?.favorita ?: false
        )

        lifecycleScope.launch {
            if (modoEdicao) {
                database.noticiaDao().atualizar(novaNoticia)
                Toast.makeText(this@AdicionarNoticiaActivity, "Notícia atualizada!", Toast.LENGTH_SHORT).show()
            } else {
                database.noticiaDao().inserir(novaNoticia)
                Toast.makeText(this@AdicionarNoticiaActivity, "Notícia adicionada!", Toast.LENGTH_SHORT).show()
                // Enviar notificação local
                enviarNotificacaoNovaNoticia(novaNoticia.titulo)
            }
            finish()
        }
    }

    /**
     * Carrega notícias da API REST usando Retrofit.
     * Converte os artigos da API para o modelo local e guarda no Room.
     */
    private fun carregarDaApi() {
        Toast.makeText(this, "A carregar da API...", Toast.LENGTH_SHORT).show()
        binding.progressBar.visibility = android.view.View.VISIBLE

        lifecycleScope.launch {
            try {
                val resposta = RetrofitCliente.apiService.obterTopHeadlines(Constantes.API_KEY)

                if (resposta.isSuccessful && resposta.body() != null) {
                    val artigos = resposta.body()!!.artigos

                    // Converter artigos da API para modelo local
                    val noticias = artigos.mapNotNull { artigo ->
                        if (artigo.titulo == null || artigo.titulo == "[Removed]") return@mapNotNull null

                        Noticia(
                            titulo = artigo.titulo,
                            descricao = artigo.descricao ?: "Sem descrição",
                            fonte = artigo.fonte.nome ?: "Desconhecida",
                            urlImagem = artigo.urlImagem ?: "",
                            urlNoticia = artigo.url ?: "",
                            dataPublicacao = converterData(artigo.dataPublicacao),
                            categoria = "Tecnologia", // categoria por defeito
                            totalFontes = 1           // API não fornece este dado diretamente
                        )
                    }

                    // Guardar no Room
                    noticias.forEach { database.noticiaDao().inserir(it) }

                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this@AdicionarNoticiaActivity, "${noticias.size} notícias carregadas!", Toast.LENGTH_LONG).show()

                    // Notificação local
                    enviarNotificacaoNovaNoticia("${noticias.size} novas notícias carregadas!")
                    finish()

                } else {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this@AdicionarNoticiaActivity, "Erro API: ${resposta.code()} — Verifica a API key", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@AdicionarNoticiaActivity, "Erro de ligação: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Converte data ISO da API ("2026-06-15T10:00:00Z") para "15/06/2026".
     */
    private fun converterData(dataIso: String?): String {
        if (dataIso == null) return "??/??/????"
        return try {
            val partes = dataIso.split("T")[0].split("-")
            "${partes[2]}/${partes[1]}/${partes[0]}"
        } catch (e: Exception) {
            "??/??/????"
        }
    }

    /**
     * Envia notificação local quando há novas notícias.
     */
    private fun enviarNotificacaoNovaNoticia(mensagem: String) {
        val notificacoes = getSharedPreferences(Constantes.PREFS_NOME, Context.MODE_PRIVATE)
            .getBoolean(Constantes.PREFS_NOTIFICACOES, true)

        if (!notificacoes) return

        // Criar canal se necessário
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                Constantes.CANAL_NOTIFICACOES,
                "Notícias",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }

        val notificacao = NotificationCompat.Builder(this, Constantes.CANAL_NOTIFICACOES)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("NewsApp — Novas notícias")
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(2, notificacao)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
