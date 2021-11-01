package com.troy.joule.repository.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @SerializedName("_id")
    @PrimaryKey
    val id: String,
    val fullName: String?,
    val email: String?,
    val password: String?,
    val latitude: Double?,
    val longitude: Double?
)
