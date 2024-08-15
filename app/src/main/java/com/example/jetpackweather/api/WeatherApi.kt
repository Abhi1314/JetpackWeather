package com.example.jetpackweather.api

import com.example.jetpackweather.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apikey:String,
        @Query("q") location :String
    ):Response<WeatherResponse>

}