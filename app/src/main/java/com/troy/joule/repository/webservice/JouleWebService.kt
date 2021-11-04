package com.troy.joule.repository.webservice

import com.troy.joule.repository.models.*
import com.troy.joule.repository.webservice.objects.Constants
import retrofit2.Response
import retrofit2.http.*

interface JouleWebService {

    /**
     * Post to api login route
     * @param user
     * @return Response<User>
     */
    @POST("/api/v1/auth/signin")
    suspend fun login(@Body user: User): Response<User>

    /**
     * Post to api register route
     * @param user
     * @return Response<User>
     */
    @POST("/api/v1/auth/signup")
    suspend fun register(@Body user: User): Response<User>

    /**
     * Get All invoices
     * @return Response<List<Invoice>>
     */
    @GET("/api/v1/invoices")
    suspend fun getAllInvoices(): Response<List<Invoice>>

    /**
     * Get All invoices
     * @return Response<List<Invoice>>
     */
    @GET("/api/v1/users/type/Driver")
    suspend fun getAllDrivers(): Response<List<Driver>>

    /**
     * Get All invoices by status
     * @param status
     * @return Response<List<Invoice>>
     */
    @GET("/api/v1/invoices/status/{status}")
    suspend fun getAllInvoicesByStatus(@Path("status") status: Constants.Status): Response<List<Invoice>>

    /**
     * Add a new invoice
     * @param uid
     * @param delivery
     * @return Response<List<Invoice>>
     */
    @POST("/api/v1/invoices/client/{uid}")
    suspend fun scheduleCollection(
        @Path("uid") uid: String,
        @Body delivery: Delivery
    ): Response<Invoice>

    /**
     * Get All User invoices
     * @param uid
     * @return Response<List<Invoice>>
     */
    @GET("/api/v1/invoices/client/{uid}")
    suspend fun getUserInvoices(@Path("uid") uid: String): Response<List<Invoice>>

    /**
     * Get All User invoice by id
     * @param uid
     * @return Response<Invoice>
     */
    @GET("/api/v1/users/{uid}/invoices/{invoice}")
    suspend fun getUserInvoicesByID(
        @Path("uid") uid: String,
        @Path("invoice") invoice: String
    ): Response<Invoice>

    /**
     * Updates user location
     * @param uid
     * @param location
     */
    @PATCH("/api/v1/users/{uid}/location")
    suspend fun updateUserLocation(@Path("uid") uid: String, @Body location: JouleLocation)
}