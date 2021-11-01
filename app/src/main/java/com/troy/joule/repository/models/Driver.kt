package com.troy.joule.repository.models


import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("_id")
    val id: String,
    val createdAt: String,
    val fullName: String,
    val latitude: Double,
    val longitude: Double,
)