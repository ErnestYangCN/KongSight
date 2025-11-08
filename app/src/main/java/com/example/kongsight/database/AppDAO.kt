/* 定义数据访问对象 */
package com.example.kongsight.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDAO {

    // Content表操作
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: ContentEntity)

    @Update
    suspend fun updateContent(content: ContentEntity)

    @Delete
    suspend fun deleteContent(content: ContentEntity)

    @Query("SELECT * FROM content WHERE id = :id")
    fun getContentById(id: Long): Flow<ContentEntity?>

    @Query("SELECT * FROM content ORDER BY creation_time DESC")
    fun getAllContents(): Flow<List<ContentEntity>>


    // User表操作
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String): Flow<UserEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE username = :username)")
    suspend fun existsByUsername(username: String): Boolean

    /*@Query("SELECT admin FROM user WHERE id = :id")
    fun getUserAdminStatus(id: Long): Flow<Boolean> */
    @Query("SELECT admin FROM user WHERE id = :id")
    suspend fun isUserAdmin(id: Long): Boolean?

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<UserEntity>>
}