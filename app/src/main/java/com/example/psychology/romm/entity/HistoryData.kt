package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryData {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var userId: Long? = null
    var videoId:Long?=null
    var time:Int?=null
    var isCollect:Boolean?=null


    constructor(userId: Long?, videoId: Long?, time: Int?, isCollect: Boolean?) {
        this.userId = userId
        this.videoId = videoId
        this.time = time
        this.isCollect = isCollect
    }
}
