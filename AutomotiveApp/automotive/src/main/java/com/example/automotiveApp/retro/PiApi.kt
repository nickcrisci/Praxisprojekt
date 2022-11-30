package com.example.automotiveApp.retro

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface PiApi {
    @Headers("Content-Type: application/json")
    @POST("sim/led")
    suspend fun changeLedMode(@Body ledTask: LedTask): Response<ApiResponse>?

    @Headers("Content-Type: application/json")
    @POST("sim/lcd")
    suspend fun showTextOnLcd(@Body lcdTask: LcdTask) : Response<ApiResponse>?

    @GET("/")
    suspend fun hello(): Response<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("log/")
    suspend fun postLog(@Body log: PiLog) : Response<Any>?
}