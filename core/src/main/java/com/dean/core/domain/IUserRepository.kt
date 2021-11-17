package com.dean.core.domain

import com.dean.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun getAllUsers(query: String?): Flow<Resource<List<User>>>

    fun getAllFollowers(username: String): Flow<Resource<List<User>>>

    fun getAllFollowing(username: String): Flow<Resource<List<User>>>

    fun getDetailUser(username: String): Flow<Resource<User>>

    fun getFavoriteUser(): Flow<List<User>>

    fun getDetailState(username: String): Flow<User>?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User): Int
}