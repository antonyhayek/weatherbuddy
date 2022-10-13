package com.antonyhayek.weatherbuddy.data.networking

import com.antonyhayek.weatherbuddy.domain.prefstore.PrefsStoreImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class RequestInterceptor(
   var prefStoreImpl: PrefsStoreImpl
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val unit = runBlocking {
            prefStoreImpl.getAppUnit().first()
        }

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("appid", Keys.API_KEY)
            .addQueryParameter("units", unit)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

}