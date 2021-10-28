package com.troy.joule.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.troy.joule.repository.database.dao.InvoiceDao
import com.troy.joule.repository.database.dao.UserDao
import com.troy.joule.repository.models.Invoice
import com.troy.joule.repository.models.User

@Database(entities = [User::class, Invoice::class], version = 1, exportSchema = false)
abstract class JouleDatabase:RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun invoiceDao(): InvoiceDao

    companion object {
        @Volatile
        private var INSTANCE: JouleDatabase? = null

        fun getInstance(context: Context): JouleDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            return Room.databaseBuilder(
                context.applicationContext,
                JouleDatabase::class.java,
                "JouleDatabase"
            ).build()

        }

    }
}