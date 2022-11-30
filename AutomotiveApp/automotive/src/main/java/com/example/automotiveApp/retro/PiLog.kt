package com.example.automotiveApp.retro

import com.google.gson.annotations.SerializedName
enum class Category {
    Info,
    Error,
    Debug,
    Warning,
    Other
}
data class PiLog(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("category") val category: Category,
)

