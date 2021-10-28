package com.troy.joule.repository.webservice.objects

object Constants {
    const val TAG = "Storm"
    const val BASE_URL = "https://delivery-io.herokuapp.com/"

    enum class Status {
        Pending, Accepted, Collected, InTransit, Delivered, Cancelled, Interrupted
    }
}