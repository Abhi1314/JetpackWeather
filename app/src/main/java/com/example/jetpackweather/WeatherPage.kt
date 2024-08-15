package com.example.jetpackweather

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpackweather.model.NetworkResponse
import com.example.jetpackweather.model.WeatherResponse
import com.example.jetpackweather.viewmodel.WeatherViewModel


@Composable
fun WeatherPage(viewModel: WeatherViewModel,context:MainActivity){
    var cityName by remember {
        mutableStateOf("")
    }
    val keyboardController= LocalSoftwareKeyboardController.current
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.animation)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val weatherResult = viewModel.weatherResult.observeAsState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly){
           OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = cityName,
            onValueChange = { cityName=it },
             label = {
                 Text(text = "Search for any location")
             }
           )
            IconButton(onClick = {
                viewModel.getData(cityName)
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription ="Search" )
            }
        }
        when(val result=weatherResult.value){
            is NetworkResponse.Error -> {
                Toast.makeText(context,result.message,Toast.LENGTH_SHORT).show()
            }
            NetworkResponse.Loading -> {
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
                    modifier = Modifier.size(250.dp) // Adjust size as needed
                )
                    }
                }
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data=result.data)
            }
            null -> {

            }
        }
    }
}

@Composable
fun WeatherDetails(data:WeatherResponse){
   Column(
       modifier = Modifier
           .fillMaxWidth()
           .padding(8.dp),
       horizontalAlignment = Alignment.CenterHorizontally

   ){
      Row (modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.Start)
      {
        IconButton(onClick = { /*TODO*/ }) {
          Icon(imageVector = Icons.Default.LocationOn, contentDescription ="Location",
              modifier = Modifier.size(40.dp)
          )
        }
          Text(text = data.location.name, fontSize = 30.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
      }
       Spacer(modifier = Modifier.width(16.dp))
       Text(text ="${data.current.temp_c} °C",
           fontSize = 50.sp,
           fontWeight = FontWeight.Bold,
           textAlign = TextAlign.Center)
       AsyncImage(
           modifier = Modifier.size(170.dp),
           model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
           contentDescription = "condition icon",
       )
       Text(text =data.current.condition.text,
           fontSize = 20.sp,
           fontWeight = FontWeight.Bold,
           textAlign = TextAlign.Center,
           color = Color.Gray)
       Spacer(modifier = Modifier.width(16.dp))
       Card {
           Column(modifier = Modifier.fillMaxWidth()) {
               Row (
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
               ){
                 WeatherData(key = "Humidity", value =data.current.humidity )
                 WeatherData(key = "Wind Speed", value =data.current.wind_kph.plus("km/h"))
               }
               Row (
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
               ){
                   WeatherData(key = "UV", value =data.current.uv )
                   WeatherData(key = "Participation", value =data.current.precip_in )
               }
               Row (
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
               ){
                   WeatherData(key = "Local Time", value =data.location.localtime.split(" ")[0])
                   WeatherData(key = "Date", value =data.location.localtime.split(" ")[1])
               }
           }
       }
   }


}
@Composable
fun WeatherData(key:String,value:String){
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold,color=Color.Gray)

    }
}