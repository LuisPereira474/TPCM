package com.example.tpcm.carAPI

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("make")
    val make: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("year")
    val year: String,

    @SerializedName("fuel_type")
    val fuel_type: String,
)
