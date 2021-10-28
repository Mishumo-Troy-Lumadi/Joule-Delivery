package com.troy.joule.repository.webservice.objects

import com.troy.joule.repository.webservice.objects.Constants.BASE_URL
import com.troy.joule.repository.webservice.JouleWebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val webService: JouleWebService by lazy {
        retrofit.create(JouleWebService::class.java)
    }
}