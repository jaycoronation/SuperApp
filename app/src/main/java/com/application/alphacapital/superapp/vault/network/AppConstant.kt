package com.application.alphacapital.superapp.vault.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AppConstant
{
    companion object {
      //  const val MAIN_URL = "https://current.coronation.in/alpha-estate-vault/api/index.php/services/"
        const val MAIN_URL = "https://vault.alphacapital.in/api/index.php/services/"
        const val API_ID : String = "YzMxYjMyMzY0Y2UxOWNhOGZjZDE1MGE0MTdlY2NlNTg="
        const val FROM_APP : String = "true"
    }
}
