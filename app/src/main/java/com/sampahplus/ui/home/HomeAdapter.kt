package com.sampahplus.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sampahplus.data.remote.response.ResultsItem
import com.sampahplus.databinding.ItemKnowledgeBinding

class HomeAdapter : ListAdapter<ResultsItem, HomeAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ItemViewHolder(val binding: ItemKnowledgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(knowledge: ResultsItem) {
            binding.apply {
                tvKnowledge.text = knowledge.namaSampah
                Glide.with(root)
                    .load(knowledge.gambarTempahSampah) // Perbaiki nama properti jika salah
                    .into(ivKnowledge)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemKnowledgeBinding =
            ItemKnowledgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val knowledge = getItem(position)
        holder.bind(knowledge)
        if (::onItemClickCallback.isInitialized) {
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(knowledge) }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(
                oldItem: ResultsItem,
                newItem: ResultsItem
            ): Boolean {
                return oldItem.id == newItem.id // Gunakan properti unik yang sesuai
            }

            override fun areContentsTheSame(
                oldItem: ResultsItem,
                newItem: ResultsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(knowledge: ResultsItem)
    }
}