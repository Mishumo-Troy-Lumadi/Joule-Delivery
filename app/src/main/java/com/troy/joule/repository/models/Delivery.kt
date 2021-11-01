package com.troy.joule.repository.models

data class Delivery(
    val distance: Double,
    val subTotal: Double,
    val promotion: Double,
    val endLatitude: Double,
    val endLongitude: Double,
    val additionalInformation: String,
    val fullName: String,
    val cell: String
)