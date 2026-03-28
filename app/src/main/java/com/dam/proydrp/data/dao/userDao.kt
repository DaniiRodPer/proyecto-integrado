package com.dam.proydrp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dam.proydrp.data.model.UserProfile

@Dao
interface UserDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserProfile)


    //update
    @Update
    suspend fun updateUser(user: UserProfile)


    //querys


    //delete
    @Query("DELETE FROM user_profile WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}