package com.sampahplus.ui.predict

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampahplus.data.remote.response.HistoryResponse
import com.sampahplus.data.remote.response.PredictResponse
import com.sampahplus.data.repository.Repository
import com.sampahplus.utils.ResultState
import kotlinx.coroutines.launch
import java.io.File

class PredictViewModel(private val repository: Repository): ViewModel() {
    fun getHistories(email:String): LiveData<ResultState<HistoryResponse>> {
        val liveData = MutableLiveData<ResultState<HistoryResponse>>()
        viewModelScope.launch {
            try {
                val result = repository.getHistories(email)
                liveData.postValue(result)
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
        return liveData
    }

    fun predictImage(file: File, email: String) : LiveData<ResultState<PredictResponse>> {
        val liveData = MutableLiveData<ResultState<PredictResponse>>()
        viewModelScope.launch {
            try {
                val result = repository.predictImage(file,email)
                liveData.postValue(result)
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
        return liveData
    }
}