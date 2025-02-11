package com.example.plantsuniverse.retrofit

import com.google.gson.annotations.SerializedName

data class PlantsSpecies(
    val data: List<Plant>,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("total") val total: Int
)

data class Plant(
    @SerializedName("id")
    val id: Int,
    @SerializedName("common_name")
    val common_name: String
)
