package com.example.psychology.bean

data class VideoAllListBean(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)

data class Data(
    val id: Int,
    val videoFatherId: Int,
    val videoImg: Any,
    val videoPath: String,
    val videoTitle: String
)