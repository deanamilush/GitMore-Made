package com.dean.core.domain

import com.bumptech.glide.load.engine.Resource
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getAllUsers(query: String?): Flow<com.dean.core.data.Resource<List<User>>>

    fun getAllFollowers(username: String): Flow<com.dean.core.data.Resource<List<User>>>

    fun getAllFollowing(username: String): Flow<com.dean.core.data.Resource<List<User>>>

    fun getDetailUser(username: String): Flow<com.dean.core.data.Resource<User>>

    fun getFavoriteUser(): Flow<List<User>>

    fun getDetailState(username: String): Flow<User>?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User): Int
}