package com.dam.proydrp.data.repository

import com.dam.proydrp.data.dao.UserDao
import com.dam.proydrp.data.model.UserProfile
import com.dam.proydrp.data.network.BaseResult // Asumo que tienes o crearás esta clase como en tu otro proyecto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    /**
     * Retrieves a user profile by its ID.
     *
     * @param userId - text to search
     * @return UserProfile or null if it doesn't exist
     *
     * @author: Daniel Rodíguez Pérez
     * @version: 1.0
     */





    /**
     * Updates an existing user profile.
     */
    suspend fun updateUser(updatedUser: UserProfile): BaseResult<UserProfile> {
        userDao.updateUser(updatedUser)
        return BaseResult.Success(updatedUser)
    }


    /**
     * Removes a user from the database.
     */
    suspend fun removeUser(userId: String) = userDao.deleteUser(userId)
}