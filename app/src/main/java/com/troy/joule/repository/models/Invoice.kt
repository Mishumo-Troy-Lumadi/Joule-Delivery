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
    val promotion: Double,
    val startLatitude: String,
    val startLongitude: String,
    val endLatitude: String,
    val endLongitude: String,
    val createdAt: String,
    val user: String,
    val driver: String?,
    val status: String
)