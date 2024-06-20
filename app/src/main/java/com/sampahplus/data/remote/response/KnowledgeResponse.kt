package com.sampahplus.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class KnowledgeResponse(
	@field:SerializedName("results")
	val knowledgeResponse: List<ResultsItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class ResultsItem(
	@field:SerializedName("jenis_tempat_sampah")
	val jenisTempatSampah: String? = null,

	@field:SerializedName("gambar_tempah_sampah")
	val gambarTempahSampah: String? = null,

	@field:SerializedName("gambar_contoh")
	val gambarContoh: String? = null,

	@field:SerializedName("nama_sampah")
	val namaSampah: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null
) : Parcelable
