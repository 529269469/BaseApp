package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
class VideoData {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var maker:Long?=null
    var video_title: String?=null//视频标题
    var video_content: String?=null//视频详细说明
    var video_path: String?=null//视频路径
    var video_type: String?=null//视频类型
    var type: Int?=null //1 视频   2音频
    var custom: Int?=null //1放松  2 冥想
    var num: Int?=null //浏览量

    constructor(
        maker: Long?,
        video_title: String?,
        video_content: String?,
        video_path: String?,
        video_type: String?,
        type: Int?,
        custom: Int?,
        num: Int?
    ) {
        this.maker = maker
        this.video_title = video_title
        this.video_content = video_content
        this.video_path = video_path
        this.video_type = video_type
        this.type = type
        this.custom = custom
        this.num = num
    }

    constructor()

}