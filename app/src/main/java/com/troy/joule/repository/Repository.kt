package com.troy.joule.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.troy.joule.repository.database.JouleDatabase
import com.troy.joule.repository.models.Delivery
import com.troy.joule.repository.models.User
import com.troy.joule.repository.webservice.JouleWebService
import com.troy.joule.repository.webservice.objects.Constants.TAG
import com.troy.joule.repository.webservice.objects.RetrofitInstance

class Repository {

    var database: JouleDatabase? = null
    var webService: JouleWebService? = null

    fun init(context: Context) {
        database = JouleDatabase.getInstance(context)
        webService = RetrofitInstance.webService
    }

    suspend fun login(user: User): Boolean {
        val response = webService?.login(user)

        if (response!!.isSuccessful) {
            database!!.clearAllTables()
            Log.d(TAG, "login: ${response.body()}")
            database!!.userDao().addUser(response.body()!!)
            // database!!.close()
            return true
        }
        return false
    }

    suspend fun register(user: User) {
        val response = webService?.register(user)

        if (response!!.isSuccessful) {
            database!!.clearAllTables()
            database!!.userDao().addUser(response.body()!!)
            // database!!.close()
        }

    }

    suspend fun scheduleCollection(uid: String, delivery: Delivery) {
        val response = webService?.scheduleCollection(uid, delivery)

        if (response!!.isSuccessful) {
            database!!.invoiceDao().addInvoice(response.body()!!)
            // database!!.close()
        }

    }

    suspend fun loadInvoices(uid: String) {
        val response = webService?.getUserInvoices(uid)

        if (response!!.isSuccessful) {

            val invoices = response.body()!!

            database!!.invoiceDao().reset()

            invoices.forEach { invoice ->
                database!!.invoiceDao().addInvoice(invoice)
            }

            // database!!.close()
        }

    }

    fun getUser(): LiveData<List<User>> {
        return database!!.userDao().getUser()
    }
}