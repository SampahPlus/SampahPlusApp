package com.sampahplus.ui.result

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sampahplus.data.remote.response.RecommendationsItem
import com.sampahplus.databinding.ItemRecomendationBinding

class ResultAdapter : ListAdapter<RecommendationsItem, ResultAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ItemViewHolder(val binding: ItemRecomendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(recomendation: RecommendationsItem) {
            binding.apply {
                tvNamaProduct.text = recomendation.namaRekom
                Glide.with(root)
                    .load(recomendation.gambar) // Perbaiki nama properti jika salah
                    .into(ivResult)
                val nilai =recomendation.harga
                tvNilai.text = "Estimasi nilai $nilai"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemRecomendationBinding =
            ItemRecomendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val recomendation = getItem(position)
        holder.bind(recomendation)
        if (::onItemClickCallback.isInitialized) {
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(recomendation) }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem.idRecomendation == newItem.idRecomendation // Gunakan properti unik yang sesuai
            }

            override fun areContentsTheSame(
                oldItem: RecommendationsItem,
                newItem: RecommendationsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(recomendation: RecommendationsItem)
    }
}