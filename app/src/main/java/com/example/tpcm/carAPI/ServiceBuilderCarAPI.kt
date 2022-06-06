package com.example.tpcm.carAPI

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilderCarAPI {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://cars-by-api-ninjas.p.rapidapi.com/v1/cars")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service:Class<T>):T{
        return retrofit.create(service)
    }

}