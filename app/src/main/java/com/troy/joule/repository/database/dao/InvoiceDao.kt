package com.troy.joule.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.troy.joule.repository.models.Invoice

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addInvoice(invoice: Invoice)

    @Query("SELECT * FROM invoice")
    fun getInvoices(): LiveData<List<Invoice>>

    @Query("SELECT * FROM invoice WHERE id = :id ")
    fun getInvoice(id: String): LiveData<Invoice>

    @Query("DELETE FROM invoice")
    fun reset()

}