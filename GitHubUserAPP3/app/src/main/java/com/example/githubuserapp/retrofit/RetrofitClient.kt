package com.example.githubuserapp.retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_Url = "https://api.github.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_Url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance = retrofit.create(Api::class.java)
}