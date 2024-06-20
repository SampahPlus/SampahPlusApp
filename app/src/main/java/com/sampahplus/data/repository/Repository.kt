package com.sampahplus.data.repository

import com.sampahplus.data.EmailRequest
import com.sampahplus.data.remote.api.ApiService
import com.sampahplus.data.remote.response.HistoryResponse
import com.sampahplus.data.remote.response.KnowledgeResponse
import com.sampahplus.data.remote.response.PredictResponse
import com.sampahplus.utils.ResultState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class Repository(
    private val apiService: ApiService,
    //    private val predictHistoryDatabase: PredictHistoryDatabase
) {
    suspend fun predictImage(
        imageFile: File,
        email : String
    ): ResultState<PredictResponse> {
        val requestBody = email.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        return try {
            val response = apiService.predictImage(multipartBody, requestBody)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    suspend fun getKnowledge():ResultState<KnowledgeResponse>{
        return try{
            val response = apiService.getKnowledge()
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    suspend fun getHistories(email:String):ResultState<HistoryResponse>{
        return try{
            val emailRequest = EmailRequest(email = email)
            val response = apiService.getHistories(emailRequest)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    suspend fun getData(id_sampah:Int):ResultState<PredictResponse>{
        return try{
            val response = apiService.getData(id_sampah)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}