package com.example.psychology.romm.dao

import androidx.room.*
import com.example.psychology.romm.entity.HistoryData
import com.example.psychology.romm.entity.User


@Dao
interface HistoryDao {

    @Query("SELECT * FROM history WHERE userId LIKE :userId AND videoId LIKE :videoId")
    fun loadAllByIds(userId:Long, videoId: Long): List<HistoryData>

    @Query("SELECT * FROM history WHERE userId LIKE :userId")
    fun loadAllByIds(userId:Long): List<HistoryData>

    @Insert
    fun insertAll(vararg history: HistoryData)

    @Delete
    fun delete(history: HistoryData)

    @Update
    fun update(history: HistoryData)
}