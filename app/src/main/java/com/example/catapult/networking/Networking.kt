package com.example.catapult.networking

import com.example.catapult.networking.serialization.AppJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Order of okhttp interceptors is important.
 * The first one intercepts the outgoing request and adds a custom header.
 * The second one logs the request and response bodies.
 */

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("x-api-key", "live_9HvL2TX7Jmx1U77FWxnVti7qVoqlhm0PW9i7A8NweP1W9O2zPFbRZ6MhMx8m326N")
            .build()
        chain.proceed(newRequest)
    })
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
    .build()