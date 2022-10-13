package com.antonyhayek.weatherbuddy.data.networking

import okhttp3.ResponseBody

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val apiException: ApiException?
    ) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}