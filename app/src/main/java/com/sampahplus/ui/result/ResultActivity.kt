package com.sampahplus.ui.result

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sampahplus.R
import com.sampahplus.data.remote.response.HistoryItem
import com.sampahplus.data.remote.response.RecommendationsItem
import com.sampahplus.data.remote.response.Results
import com.sampahplus.databinding.ActivityResultBinding
import com.sampahplus.ui.ViewModelFactory
import com.sampahplus.ui.recomendation.DetailRecomendationActivity
import com.sampahplus.utils.ResultState

class ResultActivity: AppCompatActivity() {
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityResultBinding
    private lateinit var adapter: ResultAdapter
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupData()
        binding.ivBack.setOnClickListener{
            finish()
        }
    }

    private fun setupData() {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("result", Results::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("result")
        }
        Log.d("negcek kiriman result ","result")

        if(result == null){
            val history =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("history", HistoryItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra("history")
            }
            Log.d("negcek kiriman history ","$history")
            if (history != null) {
                history.idSampah?.let {
                    viewModel.getData(it).observe(this){
                            result ->
                        when (result) {
                            is ResultState.Loading -> {
                                binding.pbLoadingHistory.visibility = View.VISIBLE
                            }

                            is ResultState.Success -> {
                                binding.pbLoadingHistory.visibility = View.GONE
                                fillData(result.data.results)

                            }

                            is ResultState.Error -> {
                                binding.pbLoadingHistory.visibility = View.GONE
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            }
                            is ResultState.Empty  ->{
                                binding.pbLoadingHistory.visibility = View.GONE

                            }
                        }
                    }
                }
            }
        }else{
            fillData(result)
        }
    }

    private fun fillData(results: Results?) {
        Glide.with(this)
            .load(results?.category?.gambar)
            .into(binding.ivResult)
        binding.apply {
            tvNamaSampah.text = results?.category?.namaSampah
            val lama=results?.category?.lamaTerurai
            tvLamaSama.text="Lama terurai $lama"
            tvJenisSampah.text= results?.category?.jenisSampah?.substringAfter("Sampah ")
            when(results?.category?.jenisSampah?.substringAfter("Sampah ")){
                "Anorganik" -> tvJenisSampah.setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.red))
                "Organik" -> tvJenisSampah.setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.green_600))
                "B3" -> {tvJenisSampah.setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.blue))
                    tvJenisSampah.text= results?.category?.jenisSampah
                }
                "Daur Ulang" -> tvJenisSampah.setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.yellow))
                "Residual" -> tvJenisSampah.setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.gray_600))
            }
        }

        val layoutManager = LinearLayoutManager(this@ResultActivity)
        binding.rvRekomendasi.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvRekomendasi.addItemDecoration(itemDecoration)

        adapter = ResultAdapter()
        adapter.setOnItemClickCallback(object : ResultAdapter.OnItemClickCallback {
            override fun onItemClicked(recomendation: RecommendationsItem) {
                val recomendationDetail = Intent(this@ResultActivity, DetailRecomendationActivity::class.java)
                recomendationDetail.putExtra("recomendation", recomendation)
                startActivity(recomendationDetail)
            }
        })
        binding.rvRekomendasi.adapter = adapter
        adapter.submitList(results?.recommendations)
    }
}