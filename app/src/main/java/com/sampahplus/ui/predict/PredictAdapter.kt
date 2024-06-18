package com.sampahplus.ui.predict

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sampahplus.R
import com.sampahplus.data.remote.response.HistoryItem
import com.sampahplus.databinding.ItemPredictBinding
import com.sampahplus.utils.DateFormatter

class PredictAdapter : ListAdapter<HistoryItem, PredictAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ItemViewHolder(val binding: ItemPredictBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(history: HistoryItem) {
            binding.apply {
                tvNamaSampah.text = history.namaSampah
                Glide.with(root)
                    .load(history.gambar) // Perbaiki nama properti jika salah
                    .into(ivResult)
                tvJenisSampah.text=history.jenisSampah?.substringAfter("Sampah ")
                when(history.jenisSampah?.substringAfter("Sampah ")){
                    "Anorganik" -> tvJenisSampah.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.red)
                    )
                    "Organik" -> tvJenisSampah.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.green_600)
                    )
                    "B3" -> {
                        tvJenisSampah.setBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.blue)
                        )
                        tvJenisSampah.text = history.jenisSampah
                    }
                    "Daur Ulang" -> tvJenisSampah.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.yellow)
                    )
                }
                val getTanggal = DateFormatter.formatDate(history.tanggal.toString())
                if (getTanggal != null) {
                    Log.d("tanggal sebelum di ubah", getTanggal)
                }
                val finalTanggal = "Diungah pada $getTanggal"
                Log.d("Tanggal setelah diubah", finalTanggal)

                tvDiunggah.text = finalTanggal
                tvLamaSama.text=history.lamaTerurai

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding: ItemPredictBinding =
            ItemPredictBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
        if (::onItemClickCallback.isInitialized) {
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(history) }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(
                oldItem: HistoryItem,
                newItem: HistoryItem
            ): Boolean {
                return oldItem.idSampah == newItem.idSampah // Gunakan properti unik yang sesuai
            }

            override fun areContentsTheSame(
                oldItem: HistoryItem,
                newItem: HistoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(history: HistoryItem)
    }
}