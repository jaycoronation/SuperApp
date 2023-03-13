package com.application.alphacapital.superapp.supermain.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient
{
    var retrofit: Retrofit? = null
    fun getClient(): Retrofit?
    {
        if (retrofit == null)
        {
            //TODO While release in Google Play Change the Level to NONE
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl("http://alphacapital.coronation.in/api/services/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }
}
