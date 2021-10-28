package com.troy.joule.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.troy.joule.repository.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM users")
    fun getUser(): LiveData<List<User>>

    @Query("DELETE FROM users")
    suspend fun reset()

}