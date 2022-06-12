package com.example.tpcm.carAPI

import com.example.tpcm.carAPI.Car
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface EndPointsCarAPI {
    @GET("/v1/cars?")
    fun getCarsDetails(@Header("X-RapidAPI-Host") host: String,
                       @Header("X-RapidAPI-Key") key: String ,
                       @Query("make") modelo:String,
                       @Query("model") fabricante:String,
                       @Query("year") ano: Int):Call<List<Car>>
}