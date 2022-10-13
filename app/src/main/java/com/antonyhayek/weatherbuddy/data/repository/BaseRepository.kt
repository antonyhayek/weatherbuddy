package com.antonyhayek.weatherbuddy.data.repository


import android.util.Log
import com.antonyhayek.weatherbuddy.data.networking.ApiException
import com.antonyhayek.weatherbuddy.data.networking.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Log.d("BaseRepository", "BaseRepository $throwable")
                when (throwable) {
                    is HttpException -> {

                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody(),
                            null
                        )

                    }
                    is ApiException -> {
                        Resource.Failure(false, throwable.code, null, throwable)
                    }
                    else -> {
                        Resource.Failure(true, null, null, null)
                    }
                }
            }
        }
    }
}