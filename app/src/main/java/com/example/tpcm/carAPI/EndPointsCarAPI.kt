package com.example.tpcm.carAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface EndPointsCarAPI {
    @GET("?model={modelo}&make={fabricante}&year={ano}")
    fun getCarsDetails(@Header("X-RapidAPI-Host") host: String, @Header("X-RapidAPI-Key") key: String , @Path("modelo") modelo:String, @Path("fabricante") fabricante:String, @Path("ano") ano: String):Call<List<Car>>
}