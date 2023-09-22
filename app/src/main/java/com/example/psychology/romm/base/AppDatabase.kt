package com.example.psychology.romm.base

import androidx.media3.extractor.ts.H264Reader
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.psychology.romm.dao.BrainDao
import com.example.psychology.romm.dao.CalendarDao
import com.example.psychology.romm.dao.HistoryDao
import com.example.psychology.romm.dao.RadarChartDao
import com.example.psychology.romm.dao.ReportDataDao
import com.example.psychology.romm.dao.UserDao
import com.example.psychology.romm.dao.VideoDataDao
import com.example.psychology.romm.entity.BrainData
import com.example.psychology.romm.entity.CalendarData
import com.example.psychology.romm.entity.HistoryData
import com.example.psychology.romm.entity.RadarChartData
import com.example.psychology.romm.entity.User
import com.example.psychology.romm.entity.VideoData
import com.example.psychology.romm.entity.ReportData

@Database(entities = [User::class,VideoData::class,ReportData::class,CalendarData::class,RadarChartData::class,HistoryData::class,BrainData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun videoDataDao(): VideoDataDao

    abstract fun ReportDataDao(): ReportDataDao

    abstract fun calendarDao(): CalendarDao

    abstract fun radarChartDao(): RadarChartDao

    abstract fun historyDao(): HistoryDao

    abstract fun brainDao(): BrainDao

}

