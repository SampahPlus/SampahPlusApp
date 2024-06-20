package com.sampahplus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampahplus.data.remote.response.KnowledgeResponse
import com.sampahplus.data.repository.Repository
import com.sampahplus.utils.ResultState
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository): ViewModel() {
    fun getKnowledge(): LiveData<ResultState<KnowledgeResponse>> {
        val liveData = MutableLiveData<ResultState<KnowledgeResponse>>()
        viewModelScope.launch {
            try {
                val result = repository.getKnowledge()
                liveData.postValue(result)
            } catch (e: Exception) {
                liveData.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
        return liveData
    }
}