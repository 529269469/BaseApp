package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blankj.utilcode.util.TimeUtils

/**
 * 历史
 */
@Entity(tableName = "calendar")
class CalendarData {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var year: Int? = null //年
    var month: Int? = null //月
    var day: Int? = null //日
    var time: Int? = null //用时
    var userId: Long? = null // 用户名
    var type: Int? = null // 1放松  2冥想
    var title: String? = null
    var path: String? = null
    var catalogue: Int? = null //1我的记录  2我的收藏  3我的报告
    var video_id:Long?=null
    var date: Int? = null
    var count: Int? = null //计数

    constructor(
        year: Int?,
        month: Int?,
        day: Int?,
        time: Int?,
        userId: Long?,
        type: Int?,
        title: String?,
        path: String?,
        catalogue: Int?,
        video_id: Long?,
        date: Int?,
        count: Int?
    ) {
        this.year = year
        this.month = month
        this.day = day
        this.time = time
        this.userId = userId
        this.type = type
        this.title = title
        this.path = path
        this.catalogue = catalogue
        this.video_id = video_id
        this.date = date
        this.count = count
    }
}