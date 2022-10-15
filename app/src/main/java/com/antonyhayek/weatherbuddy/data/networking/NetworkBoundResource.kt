package com.antonyhayek.weatherbuddy.data.networking

import kotlinx.coroutines.flow.*
import retrofit2.HttpException

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading)

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map {

                when (throwable) {
                    is HttpException -> {
                        if (throwable.code() == 401) {
                            Resource.Failure(false, null, null, null)
                        } else {
                            Resource.Failure(
                                false,
                                throwable.code(),
                                throwable.response()!!.errorBody(),
                                null
                            )
                        }
                    }
                    is ApiException -> {
                        Resource.Failure(false, null, null, throwable)
                    }
                    else -> {
                        Resource.Failure(true, null, null, null)
                    }
                }
            }
        }

    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}