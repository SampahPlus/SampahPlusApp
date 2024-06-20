package com.sampahplus.data.remote.api

import com.sampahplus.data.EmailRequest
import com.sampahplus.data.remote.response.HistoryResponse
import com.sampahplus.data.remote.response.KnowledgeResponse
import com.sampahplus.data.remote.response.PredictResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("knowledge")
    suspend fun getKnowledge(): KnowledgeResponse

    @POST("histories")
    suspend fun getHistories(
        @Body email : EmailRequest
    ): HistoryResponse

    @GET("getData/{id_sampah}")
    suspend fun getData (@Path("id_sampah") id_sampah: Int):PredictResponse

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part file: MultipartBody.Part,
        @Part("email") email: RequestBody
    ): PredictResponse


}