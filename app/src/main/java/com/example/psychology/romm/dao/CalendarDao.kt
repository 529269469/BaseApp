package com.example.psychology.romm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.psychology.romm.entity.CalendarData
import com.example.psychology.romm.entity.User

@Dao
interface CalendarDao {
    @Insert
    fun insertAll(vararg calendar: CalendarData)

    @Query("SELECT * FROM calendar WHERE  year LIKE :year AND " +
                "month LIKE :month AND catalogue LIKE :catalogue AND userId LIKE:userId")
    fun findByMonth(year: String,month: String,catalogue:Int, userId: Long): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE  year LIKE :year AND " +
            "month LIKE :month AND day LIKE :day AND catalogue LIKE :catalogue AND userId LIKE:userId")
    fun findByDay(year: String,month: String, day: String,catalogue:Int, userId: Long): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE  year LIKE :year AND " +
            "month LIKE :month AND day LIKE :day AND catalogue LIKE :catalogue AND type LIKE :type")
    fun findByDay(year: String,month: String, day: String,catalogue:Int, type:Int): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE " +
                "date =:date AND catalogue =:catalogue AND userId =:userId")
    fun findByDay(date: Int, catalogue: Int, userId: Long): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE " +
            "date <=:date AND catalogue =:catalogue AND userId =:userId")
    fun findByDays(date: Int, catalogue: Int, userId: Long): List<CalendarData>


    @Query("SELECT * FROM calendar WHERE " +
                " date =:day AND type = :type AND catalogue = :catalogue AND userId =:userId")
    fun findByDay(
        day: Int,
        type: Int,
        catalogue: Int,
        userId: Long
    ): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE " +
            " date <=:day AND type = :type AND catalogue = :catalogue AND userId =:userId")
    fun findByDays(
        day: Int,
        type: Int,
        catalogue: Int,
        userId: Long
    ): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE video_id =:video_id AND catalogue =:catalogue AND userId =:userId")
    fun findByCatalogue(video_id: Long, catalogue: Int, userId: Long): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE catalogue =:catalogue AND userId =:userId AND type in (:type) ")
    fun findByCatalogue(catalogue: Int, userId: Long,type: Int): List<CalendarData>

    @Query("SELECT * FROM calendar WHERE catalogue =:catalogue AND userId =:userId")
    fun findByCatalogue(catalogue: Int, userId: Long): List<CalendarData>

    @Update
    fun update(calendarData: CalendarData)

    @Delete
    fun delete(calendarData: CalendarData)
}