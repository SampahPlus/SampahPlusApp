package com.sampahplus.ui.knowledge

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sampahplus.data.remote.response.ResultsItem
import com.sampahplus.databinding.ActivityKnowledgeBinding

class KnowledgeActivity: AppCompatActivity(){
    private lateinit var binding: ActivityKnowledgeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKnowledgeBinding.inflate(layoutInflater)
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
        val knowledge = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("knowledge", ResultsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("knowledge")
        }

        Glide.with(this)
            .load(knowledge?.gambarTempahSampah)
            .into(binding.ivTempatSampah)
        Glide.with(this)
            .load(knowledge?.gambarContoh)
            .into(binding.ivKnowledge)
        binding.apply {
            tvTempatSampah.text = knowledge?.jenisTempatSampah
            tvNamaSampah.text = knowledge?.jenisTempatSampah
            val namaSampah = knowledge?.namaSampah
            val penjelesan = knowledge?.deskripsi
            tvJenisSampah.text="$namaSampah $penjelesan"
        }
    }
}