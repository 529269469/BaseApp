package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "radar_chart")
class RadarChartData {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var reportId: Long = 0
    var type: Int? = null // 1专注 2放松 3压力 4情绪 5疲劳 6心率 7hrv 8血氧
    var number: Int? = null //排序

    var num: Int? = null
    var num2: Float? = null


    constructor(reportId: Long, type: Int?, number: Int?, num: Int?, num2: Float?) {
        this.reportId = reportId
        this.type = type
        this.number = number
        this.num = num
        this.num2 = num2
    }



}

