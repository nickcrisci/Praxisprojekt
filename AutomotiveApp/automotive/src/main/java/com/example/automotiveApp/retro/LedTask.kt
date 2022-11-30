package com.example.automotiveApp.retro

import com.google.gson.annotations.SerializedName

data class LedTask(
    @SerializedName("mode") val ledMode: String
)

data class LcdTask(
    @SerializedName("text") val lcdText: String
)
