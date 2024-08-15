package com.example.jetpackweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackweather.ui.theme.JetpackWeatherTheme
import com.example.jetpackweather.viewmodel.WeatherViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val weatherViewModel=ViewModelProvider(this)[WeatherViewModel::class.java]
        setContent {
            JetpackWeatherTheme(false,false) {
                var showSplash by remember { mutableStateOf(true) }

                // LaunchedEffect to handle splash screen timing
                LaunchedEffect(Unit) {
                    delay(3000L) // Delay of 3 seconds
                    showSplash = false // Hide splash screen after delay
                }

                if (showSplash) {
                    Splash() // Display Splash screen
                } else {
                    WeatherPage(weatherViewModel, this) // Transition to WeatherPage
                }
//                WeatherPage(weatherViewModel,this)
            }
        }
    }

    @Composable
    fun Splash(){
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.animation)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
          Row (
              modifier = Modifier.padding(0.dp,150.dp),
              horizontalArrangement = Arrangement.Center,
          ){
              LottieAnimation(
                  composition = composition,
                  progress = progress,
                  modifier = Modifier.size(350.dp),
              )
          }
        }

    }
}