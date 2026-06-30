package com.example.newsapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapter.NoticiaAdapter
import com.example.newsapp.database.AppDatabase
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.Constantes
import com.example.newsapp.model.Noticia
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Activity principal — lista de notícias.
 *
 * PARTE DE: Gabi
 * Cobre: RecyclerView, pesquisa em tempo real, filtros/ordenação,
 *        modo escuro (SharedPreferences), swipe to delete
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: NoticiaAdapter

    private val prefs by lazy {
        getSharedPreferences(Constantes.PREFS_NOME, Context.MODE_PRIVATE)
    }

    // Estado atual dos filtros
    private var textoPesquisa: String = ""
    private var categoriaAtual: String = "Todas"
    private var ordenacaoAtual: String = "data"  // "data", "titulo", "fontes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aplicarModoEscuro()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        database = AppDatabase.obterInstancia(this)

        configurarRecyclerView()
        configurarSwitch()
        observarNoticias()

        // FAB — adicionar notícia manualmente
        binding.fabAdicionarNoticia.setOnClickListener {
            startActivity(Intent(this, AdicionarNoticiaActivity::class.java))
        }
    }

    /**
     * Configura a RecyclerView com adapter, LayoutManager e swipe to delete.
     */
    private fun configurarRecyclerView() {
        adapter = NoticiaAdapter(
            onItemClick = { noticia ->
                val intent = Intent(this, DetalheNoticiaActivity::class.java)
                intent.putExtra(Constantes.EXTRA_NOTICIA, noticia)
                startActivity(intent)
            },
            onFavoritaClick = { noticia ->
                lifecycleScope.launch {
                    val atualizada = noticia.copy(favorita = !noticia.favorita)
                    database.noticiaDao().atualizar(atualizada)
                    val msg = if (atualizada.favorita) "Adicionada aos favoritos" else "Removida dos favoritos"
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.recyclerViewNoticias.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNoticias.adapter = adapter

        // Swipe para eliminar — o AlertDialog de confirmação fica no DetalheNoticiaActivity
        // Aqui eliminamos diretamente com swipe (operação rápida da lista)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicao = viewHolder.adapterPosition
                val noticia = adapter.obterNoticiaNaPosicao(posicao)
                lifecycleScope.launch {
                    database.noticiaDao().eliminar(noticia)
                    Toast.makeText(this@MainActivity, "Notícia removida", Toast.LENGTH_SHORT).show()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewNoticias)
    }

    /**
     * Configura o Switch de modo escuro (acesso rápido na MainActivity).
     */
    private fun configurarSwitch() {
        binding.switchModoEscuro.isChecked = prefs.getBoolean(Constantes.PREFS_MODO_ESCURO, false)
        binding.switchModoEscuro.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(Constantes.PREFS_MODO_ESCURO, isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    /**
     * Observa a lista de notícias com base nos filtros ativos.
     * Usa Flow do Room para atualização automática da UI.
     */
    private fun observarNoticias() {
        lifecycleScope.launch {
            val flow = when {
                textoPesquisa.isNotEmpty() ->
                    database.noticiaDao().pesquisar(textoPesquisa)
                categoriaAtual != "Todas" ->
                    database.noticiaDao().obterPorCategoria(categoriaAtual)
                ordenacaoAtual == "titulo" ->
                    database.noticiaDao().obterOrdenadasPorTitulo()
                ordenacaoAtual == "fontes" ->
                    database.noticiaDao().obterOrdenadasPorFontes()
                else ->
                    database.noticiaDao().obterTodas()
            }

            flow.collectLatest { lista ->
                adapter.submitList(lista)
                binding.tvListaVazia.visibility =
                    if (lista.isEmpty()) android.view.View.VISIBLE
                    else android.view.View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Configurar SearchView para pesquisa em tempo real
        val searchItem = menu.findItem(R.id.action_pesquisa)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Pesquisar notícias..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                textoPesquisa = newText ?: ""
                observarNoticias()
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Filtros por categoria
            R.id.action_filtro_todas -> { categoriaAtual = "Todas"; observarNoticias(); true }
            R.id.action_filtro_tecnologia -> { categoriaAtual = "Tecnologia"; observarNoticias(); true }
            R.id.action_filtro_desporto -> { categoriaAtual = "Desporto"; observarNoticias(); true }
            R.id.action_filtro_politica -> { categoriaAtual = "Política"; observarNoticias(); true }
            R.id.action_filtro_saude -> { categoriaAtual = "Saúde"; observarNoticias(); true }

            // Ordenação
            R.id.action_ordenar_data -> { ordenacaoAtual = "data"; observarNoticias(); true }
            R.id.action_ordenar_titulo -> { ordenacaoAtual = "titulo"; observarNoticias(); true }
            R.id.action_ordenar_fontes -> { ordenacaoAtual = "fontes"; observarNoticias(); true }

            // Definições
            R.id.action_definicoes -> {
                startActivity(Intent(this, DefinicoesActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Aplica o modo escuro guardado nas SharedPreferences.
     */
    private fun aplicarModoEscuro() {
        val modoEscuro = getSharedPreferences(Constantes.PREFS_NOME, Context.MODE_PRIVATE)
            .getBoolean(Constantes.PREFS_MODO_ESCURO, false)
        AppCompatDelegate.setDefaultNightMode(
            if (modoEscuro) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    // Recarregar lista ao voltar de outra Activity
    override fun onResume() {
        super.onResume()
        observarNoticias()
    }
}
