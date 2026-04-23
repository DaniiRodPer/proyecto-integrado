package com.dam.dovelia.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dam.dovelia.data.model.UserProfile

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :userId")
    suspend fun getUserById(userId: String): UserProfile?

    @Query("SELECT * FROM user_profile")
    suspend fun getAllUsers(): List<UserProfile>

    @Query("DELETE FROM user_profile WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}