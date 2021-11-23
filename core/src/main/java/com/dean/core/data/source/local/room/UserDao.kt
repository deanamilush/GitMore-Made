package com.dean.core.data.source.local.room

import androidx.room.*
import com.dean.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * from user_table ORDER BY login ASC")
    fun getFavoriteUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user_table WHERE login = :username")
    fun getDetailState(username: String): Flow<UserEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity?)

    @Delete
    suspend fun deleteUser(user: UserEntity): Int
}