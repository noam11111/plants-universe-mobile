package com.example.plantsuniverse.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantsClient {

    @GET("api/species-list")
    fun getPlantsSpecies(
        @Query("key") apiKey: String,
        @Query("page") page: Int
    ): Call<PlantsSpecies>
}
