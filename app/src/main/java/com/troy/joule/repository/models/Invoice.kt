package com.troy.joule.repository.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "invoice")
data class Invoice(
    @SerializedName("_id")
    @PrimaryKey
    val id: String,
    val distance: Double,
    val subTotal: Double,
    val Promotion: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val user: String,
    val driver: String?,
    val status: String
)