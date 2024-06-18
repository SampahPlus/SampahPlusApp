package com.sampahplus.ui.recomendation

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sampahplus.data.remote.response.RecommendationsItem
import com.sampahplus.databinding.ActivityDetailRecomendationBinding

class DetailRecomendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecomendationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupData()
        setClickListener()
    }


    private fun setClickListener() {
        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    private fun setupData() {
        val recomendation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("recomendation", RecommendationsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("recomendation")
        }

        Glide.with(this)
            .load(recomendation?.gambar)
            .into(binding.ivRecomendation)
        binding.apply {
            tvNilai.text = recomendation?.harga
            tvNamaProduct.text = recomendation?.namaRekom
            tvTutorialDetail.text = recomendation?.langkahPembuatan
        }
    }
}