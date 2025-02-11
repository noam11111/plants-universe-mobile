package com.example.plantsuniverse.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantsRepository {
    fun getPlants(callback: (Result<List<Plant>>) -> Unit)  {
        val request = NetworkClient.plantsSpeciesClient.getPlantsSpecies(
            page = 1,
            apiKey = "sk-SvHT67ab603380dbd8605"
        )

        request.enqueue(object : Callback<PlantsSpecies> {
            override fun onResponse(call: Call<PlantsSpecies>, response: Response<PlantsSpecies>) {
                val rawResponse = response.body()?.toString() ?: response.errorBody()?.string()

                if (response.isSuccessful) {
                    val plants = response.body()?.data
                    if (plants != null) {
                        callback(Result.success(plants))
                    } else {
                        callback(Result.failure(Exception("No plants found")))
                    }
                } else {
                    callback(Result.failure(Exception("Response error: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<PlantsSpecies>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}