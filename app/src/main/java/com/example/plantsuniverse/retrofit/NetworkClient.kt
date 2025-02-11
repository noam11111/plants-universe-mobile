package com.example.plantsuniverse.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)  // Set connection timeout
            .readTimeout(60, TimeUnit.SECONDS)     // Set read timeout
            .writeTimeout(60, TimeUnit.SECONDS)    // Set write timeout
            .build()
    }

    val plantsSpeciesClient: PlantsClient by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://perenual.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(PlantsClient::class.java)
    }
}