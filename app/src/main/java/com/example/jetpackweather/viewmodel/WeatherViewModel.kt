package com.example.jetpackweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackweather.api.Constant
import com.example.jetpackweather.api.RetrofitInstance
import com.example.jetpackweather.model.NetworkResponse
import com.example.jetpackweather.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi=RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherResponse>>()
     val weatherResult : LiveData<NetworkResponse<WeatherResponse>> =_weatherResult
    fun getData(location: String) {
        Log.d("location", location)
        _weatherResult.value=NetworkResponse.Loading
        viewModelScope.launch {
            val response = weatherApi.getCurrentWeather(Constant.apikey, location)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("response", body.toString())
                    _weatherResult.value = NetworkResponse.Success(body)
                } else {
                    _weatherResult.value = NetworkResponse.Error("Empty response body")
                    Log.d("error", "Empty response body")
                }
            } else {
                _weatherResult.value = NetworkResponse.Error("Something went wrong")
                Log.d("error", response.message().toString())
            }
        }
    }
}