package com.sampahplus.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("histories")
	val histories: Histories? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class HistoryItem(

	@field:SerializedName("id_sampah")
	val idSampah: Int? = null,

	@field:SerializedName("nama_sampah")
	val namaSampah: String? = null,

	@field:SerializedName("lama_terurai")
	val lamaTerurai: String? = null,

	@field:SerializedName("id_history")
	val idHistory: Int? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("jenis_sampah")
	val jenisSampah: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable

@Parcelize
data class Histories(

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null,

	//email
	@field:SerializedName("user")
	val user: String? = null
) : Parcelable
