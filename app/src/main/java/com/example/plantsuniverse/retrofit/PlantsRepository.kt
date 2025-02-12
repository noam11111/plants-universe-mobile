package com.example.plantsuniverse.retrofit

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantsRepository {
        fun getPlants(callback: (Result<List<Plant>>) -> Unit, progressBar: ProgressBar? = null) {
            progressBar?.visibility = View.VISIBLE

            val request = NetworkClient.plantsSpeciesClient.getPlantsSpecies(
                page = 1,
                apiKey = "sk-SvHT67ab603380dbd8605"
            )

            request.enqueue(object : Callback<PlantsSpecies> {
                override fun onResponse(call: Call<PlantsSpecies>, response: Response<PlantsSpecies>) {
                    progressBar?.visibility = View.GONE

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
                    progressBar?.visibility = View.GONE
                    callback(Result.failure(t))
                }
            })
        }
    }