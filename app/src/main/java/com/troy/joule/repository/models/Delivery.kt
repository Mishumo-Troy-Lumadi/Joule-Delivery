package com.troy.joule.repository.models

import java.io.Serializable

data class Delivery(
    val distance: Double,
    val subTotal: Double,
    val promotion: Double,
    val startLatitude: String,
    val startLongitude: String,
    val destination:String,
    val endLatitude: String,
    val endLongitude: String,
    val additionalInformation: String,
    val fullName: String,
    val cell: String
): Serializable