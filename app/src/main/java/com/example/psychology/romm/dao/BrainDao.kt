package com.example.psychology.romm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.psychology.romm.entity.BrainData
import com.example.psychology.romm.entity.CalendarData
import com.example.psychology.romm.entity.RadarChartData

@Dao
interface BrainDao {

    @Insert
    fun insertAll(vararg brainData: BrainData)


    @Query("SELECT * FROM brain WHERE reportId LIKE :reportId ")
    fun findByMonth(reportId: Long): List<BrainData>




}