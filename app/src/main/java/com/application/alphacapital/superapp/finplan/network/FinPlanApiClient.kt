package com.application.alphacapital.superapp.finplan.network

import com.application.alphacapital.superapp.finplan.utils.AppConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FinPlanApiClient
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
                    .baseUrl(AppConstant.MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit
    }
}
