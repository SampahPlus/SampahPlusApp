package com.sampahplus.data.di

import android.content.Context
import com.sampahplus.data.remote.api.ApiConfig
import com.sampahplus.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}