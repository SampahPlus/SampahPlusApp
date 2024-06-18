package com.sampahplus.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampahplus.data.remote.response.PredictResponse
import com.sampahplus.data.repository.Repository
import com.sampahplus.utils.ResultState
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: Repository) : ViewModel() {

    fun getData(id_sampah:Int) : LiveData<ResultState<PredictResponse>> {
        val liveData = MutableLiveData<ResultState<PredictResponse>>()
        viewModelScope.launch {
            try {
                val result = repository.getData(id_sampah)
                liveData.postValue(result)
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
        return liveData
    }

}