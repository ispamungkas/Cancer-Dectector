package com.dicoding.asclepius.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.databinding.ItemArticleBinding
import com.dicoding.asclepius.model.ArticlesItem

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ArticlerHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, DIFF)

    inner class ArticlerHolder(val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleAdapter.ArticlerHolder {
        val item = ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ArticlerHolder(item)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ArticlerHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.binding.tvTitle.text = data.title
        holder.binding.tvAuthor.text = data.author
        holder.binding.tvDescription.text = data.description
        Glide.with(holder.itemView.context)
            .load(data.urlToImage)
            .into(holder.binding.image)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun saveData(data: List<ArticlesItem?>) {
        asyncListDiffer.submitList(data)
    }

    companion object {
        val DIFF = object: DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return newItem.content == oldItem.content
            }

        }
    }
}