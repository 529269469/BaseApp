package com.example.psychology.romm.dao

import androidx.room.*
import com.example.psychology.romm.entity.User


@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE user_name LIKE :user_name AND password LIKE :password")
    fun findByPassword(user_name: String,password:String): List<User>

    @Query("SELECT * FROM users WHERE user_name = :user_name ")
    fun findByName(user_name: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)
}