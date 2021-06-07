package com.androiddevs.mvvmnewsapp.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {
    companion object {
        var gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(CountriesAPI::class.java)
        }
    }
}