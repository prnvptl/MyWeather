package com.example.myweather.data

import com.example.myweather.data.network.response.CurrentWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "84a7bee1ffe04f048a2190459210101"
const val BASE_URL = "https://api.weatherapi.com/v1/"

// http://api.weatherapi.com/v1/current.json?key=84a7bee1ffe04f048a2190459210101&q=Clifton@lang=en

interface WeatherApiService {

    @GET("current.json")
    fun getCurrentWeather(
                @Query("q") location: String,
                @Query("lang") languageCode: String= "en"
    ): retrofit2.Call<CurrentWeatherResponse>

    companion object {
        operator fun invoke(): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()

                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherApiService::class.java)
        }
    }
}