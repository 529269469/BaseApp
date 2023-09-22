package com.example.psychology.romm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.psychology.romm.entity.ReportData
import com.example.psychology.romm.entity.User

@Dao
interface ReportDataDao {
    @Query("SELECT * FROM report")
    fun getAll(): List<ReportData>

    @Query("SELECT * FROM report WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<ReportData>

    @Query("SELECT * FROM report WHERE id LIKE :id LIMIT 1")
    fun loadAllById(id: Long): ReportData

    @Query("SELECT * FROM report WHERE userId LIKE :userId")
    fun loadAllByUserId(userId: Long): List<ReportData>

    @Query("SELECT * FROM report WHERE year LIKE :year and month like :month and day like :day and userId like :userId")
    fun findById(year: Int,month: Int,day: Int,userId: Long):  List<ReportData>

    @Insert
    fun insertAll(vararg video_paths: ReportData)

    @Delete
    fun delete(video: ReportData)

    @Update
    fun update(reportData: ReportData)

}