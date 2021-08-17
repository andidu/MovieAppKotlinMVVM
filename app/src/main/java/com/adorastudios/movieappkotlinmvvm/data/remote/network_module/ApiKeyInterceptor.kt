package com.adorastudios.movieappkotlinmvvm.data.remote.network_module

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {

    companion object {
        private const val API_KEY = "977beefd06919d9a9670eb412dfb7c87"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()
        val urlBuilder = origin.url.newBuilder()
        val url = urlBuilder
            .addQueryParameter("api_key", API_KEY)
            .build()

        val requestBuilder = origin.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}