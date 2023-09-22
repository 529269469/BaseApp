package com.example.psychology.romm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.psychology.romm.entity.CalendarData
import com.example.psychology.romm.entity.RadarChartData

@Dao
interface RadarChartDao {

    @Insert
    fun insertAll(vararg radarChartDao: RadarChartData)


    @Query("SELECT * FROM radar_chart WHERE reportId LIKE :reportId AND type LIKE:type")
    fun findByMonth(reportId: Long, type: Int): List<RadarChartData>


    @Query("SELECT * FROM radar_chart WHERE  type LIKE:type")
    fun findByAll( type: Int): List<RadarChartData>

}