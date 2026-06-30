package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemNoticiaBinding
import com.example.newsapp.model.Noticia

/**
 * Adapter para a RecyclerView da lista de notícias.
 * Usa ListAdapter com DiffUtil para atualizações eficientes.
 * ViewBinding em cada ViewHolder.
 */
class NoticiaAdapter(
    private val onItemClick: (Noticia) -> Unit,      // Clique num item — abre detalhe
    private val onFavoritaClick: (Noticia) -> Unit   // Clique na estrela — toggle favorito
) : ListAdapter<Noticia, NoticiaAdapter.NoticiaViewHolder>(NoticiaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
        val binding = ItemNoticiaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoticiaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Usado para swipe to delete
    fun obterNoticiaNaPosicao(position: Int): Noticia = getItem(position)

    /**
     * ViewHolder — representa um item da lista.
     */
    inner class NoticiaViewHolder(private val binding: ItemNoticiaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(noticia: Noticia) {
            // Preencher dados
            binding.tvTitulo.text = noticia.titulo
            binding.tvFonte.text = noticia.fonte
            binding.tvCategoria.text = noticia.categoria
            binding.tvData.text = noticia.dataPublicacao

            // Ícone favorita
            val icFavorita = if (noticia.favorita)
                android.R.drawable.btn_star_big_on
            else
                android.R.drawable.btn_star_big_off
            binding.btnFavorita.setImageResource(icFavorita)

            // Clique no item
            binding.root.setOnClickListener { onItemClick(noticia) }

            // Clique no favorito
            binding.btnFavorita.setOnClickListener { onFavoritaClick(noticia) }
        }
    }

    /**
     * DiffUtil — compara itens para atualizar só o necessário.
     */
    class NoticiaDiffCallback : DiffUtil.ItemCallback<Noticia>() {
        override fun areItemsTheSame(oldItem: Noticia, newItem: Noticia): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Noticia, newItem: Noticia): Boolean =
            oldItem == newItem
    }
}
