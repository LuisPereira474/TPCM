package com.example.tpcm.models

import java.util.*

data class Message(
    val message: String,
    val nameUser: String,
    val idBoleia: String,
    val date: Date,
    val idUser: String
)

