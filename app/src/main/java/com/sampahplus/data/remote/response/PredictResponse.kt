package com.sampahplus.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("results")
	val results: Results? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class RecommendationsItem(

	@field:SerializedName("id_rekomendasi")
	val idRecomendation: Int? =null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("nama_rekom")
	val namaRekom: String? = null,

	@field:SerializedName("langkah_pembuatan")
	val langkahPembuatan: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
) : Parcelable

@Parcelize
data class Category(


	@field:SerializedName("id_sampah")
	val idSampah: Int? = null,

	@field:SerializedName("nama_sampah")
	val namaSampah: String? = null,

	@field:SerializedName("lama_terurai")
	val lamaTerurai: String? = null,

	@field:SerializedName("jenis_sampah")
	val jenisSampah: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
) : Parcelable

@Parcelize
data class Results(

	@field:SerializedName("category")
	val category: Category? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
) : Parcelable
