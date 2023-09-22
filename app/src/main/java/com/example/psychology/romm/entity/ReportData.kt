package com.example.psychology.romm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//我的报告
@Entity(tableName = "report")
class ReportData {
    @PrimaryKey(autoGenerate = true)
    var id: Long ? = null
    var userId: Long? = null // 用户名
    var year: Int? = null //年
    var month: Int? = null //月
    var day: Int? = null //日

    var report_dirll_score: Int?=null//训练有效性得分
    var report_mind_score: Int?=null//身心状态得分

    var content1:String?=null
    var content2:String?=null
    var content3:String?=null
    var content4:String?=null

    //专注
    var zhuanzhu_front:Int?=null//前测
    var zhuanzhu_centre :Int?=null//中测
    var zhuanzhu_later :Int?=null//后测
    var zhuanzhu_front_average:Int?=null//前测平均
    var zhuanzhu_centre_average :Int?=null//中测平均
    var zhuanzhu_later_average :Int?=null//后测平均
    var zhuanzhu_front_average_root:Int?=null//前测平均
    var zhuanzhu_centre_average_root :Int?=null//中测平均
    var zhuanzhu_later_average_root :Int?=null//后测平均
    var zhuanzhu_effectiveness:Int?=null


    //放松
    var fangsong_front:Int?=null
    var fangsong_centre:Int?=null
    var fangsong_later:Int?=null
    var fangsong_front_average:Int?=null
    var fangsong_centre_average:Int?=null
    var fangsong_later_average:Int?=null
    var fangsong_front_average_root:Int?=null
    var fangsong_centre_average_root:Int?=null
    var fangsong_later_average_root:Int?=null
    var fangsong_effectiveness:Int?=null

    //压力
    var yali_front:Int?=null
    var yali_centre:Int?=null
    var yali_later:Int?=null
    var yali_front_average:Int?=null
    var yali_centre_average:Int?=null
    var yali_later_average:Int?=null
    var yali_front_average_root:Int?=null
    var yali_centre_average_root:Int?=null
    var yali_later_average_root:Int?=null
    var yali_effectiveness:Int?=null

    //情绪
    var qingxu_front:Int?=null
    var qingxu_centre:Int?=null
    var qingxu_later:Int?=null
    var qingxu_front_average:Int?=null
    var qingxu_centre_average:Int?=null
    var qingxu_later_average:Int?=null
    var qingxu_front_average_root:Int?=null
    var qingxu_centre_average_root:Int?=null
    var qingxu_later_average_root:Int?=null
    var qingxu_effectiveness:Int?=null

    //疲劳
    var pilao_front:Int?=null
    var pilao_centre:Int?=null
    var pilao_later:Int?=null
    var pilao_front_average:Int?=null
    var pilao_centre_average:Int?=null
    var pilao_later_average:Int?=null
    var pilao_front_average_root:Int?=null
    var pilao_centre_average_root:Int?=null
    var pilao_later_average_root:Int?=null
    var pilao_effectiveness:Int?=null

    constructor(
        id: Long?,
        userId: Long?,
        year: Int?,
        month: Int?,
        day: Int?,
        report_dirll_score: Int?,
        report_mind_score: Int?,
        content1: String?,
        content2: String?,
        content3: String?,
        content4: String?,
        zhuanzhu_front: Int?,
        zhuanzhu_centre: Int?,
        zhuanzhu_later: Int?,
        zhuanzhu_front_average: Int?,
        zhuanzhu_centre_average: Int?,
        zhuanzhu_later_average: Int?,
        zhuanzhu_front_average_root: Int?,
        zhuanzhu_centre_average_root: Int?,
        zhuanzhu_later_average_root: Int?,
        zhuanzhu_effectiveness: Int?,
        fangsong_front: Int?,
        fangsong_centre: Int?,
        fangsong_later: Int?,
        fangsong_front_average: Int?,
        fangsong_centre_average: Int?,
        fangsong_later_average: Int?,
        fangsong_front_average_root: Int?,
        fangsong_centre_average_root: Int?,
        fangsong_later_average_root: Int?,
        fangsong_effectiveness: Int?,
        yali_front: Int?,
        yali_centre: Int?,
        yali_later: Int?,
        yali_front_average: Int?,
        yali_centre_average: Int?,
        yali_later_average: Int?,
        yali_front_average_root: Int?,
        yali_centre_average_root: Int?,
        yali_later_average_root: Int?,
        yali_effectiveness: Int?,
        qingxu_front: Int?,
        qingxu_centre: Int?,
        qingxu_later: Int?,
        qingxu_front_average: Int?,
        qingxu_centre_average: Int?,
        qingxu_later_average: Int?,
        qingxu_front_average_root: Int?,
        qingxu_centre_average_root: Int?,
        qingxu_later_average_root: Int?,
        qingxu_effectiveness: Int?,
        pilao_front: Int?,
        pilao_centre: Int?,
        pilao_later: Int?,
        pilao_front_average: Int?,
        pilao_centre_average: Int?,
        pilao_later_average: Int?,
        pilao_front_average_root: Int?,
        pilao_centre_average_root: Int?,
        pilao_later_average_root: Int?,
        pilao_effectiveness: Int?
    ) {
        this.id = id
        this.userId = userId
        this.year = year
        this.month = month
        this.day = day
        this.report_dirll_score = report_dirll_score
        this.report_mind_score = report_mind_score
        this.content1 = content1
        this.content2 = content2
        this.content3 = content3
        this.content4 = content4
        this.zhuanzhu_front = zhuanzhu_front
        this.zhuanzhu_centre = zhuanzhu_centre
        this.zhuanzhu_later = zhuanzhu_later
        this.zhuanzhu_front_average = zhuanzhu_front_average
        this.zhuanzhu_centre_average = zhuanzhu_centre_average
        this.zhuanzhu_later_average = zhuanzhu_later_average
        this.zhuanzhu_front_average_root = zhuanzhu_front_average_root
        this.zhuanzhu_centre_average_root = zhuanzhu_centre_average_root
        this.zhuanzhu_later_average_root = zhuanzhu_later_average_root
        this.zhuanzhu_effectiveness = zhuanzhu_effectiveness
        this.fangsong_front = fangsong_front
        this.fangsong_centre = fangsong_centre
        this.fangsong_later = fangsong_later
        this.fangsong_front_average = fangsong_front_average
        this.fangsong_centre_average = fangsong_centre_average
        this.fangsong_later_average = fangsong_later_average
        this.fangsong_front_average_root = fangsong_front_average_root
        this.fangsong_centre_average_root = fangsong_centre_average_root
        this.fangsong_later_average_root = fangsong_later_average_root
        this.fangsong_effectiveness = fangsong_effectiveness
        this.yali_front = yali_front
        this.yali_centre = yali_centre
        this.yali_later = yali_later
        this.yali_front_average = yali_front_average
        this.yali_centre_average = yali_centre_average
        this.yali_later_average = yali_later_average
        this.yali_front_average_root = yali_front_average_root
        this.yali_centre_average_root = yali_centre_average_root
        this.yali_later_average_root = yali_later_average_root
        this.yali_effectiveness = yali_effectiveness
        this.qingxu_front = qingxu_front
        this.qingxu_centre = qingxu_centre
        this.qingxu_later = qingxu_later
        this.qingxu_front_average = qingxu_front_average
        this.qingxu_centre_average = qingxu_centre_average
        this.qingxu_later_average = qingxu_later_average
        this.qingxu_front_average_root = qingxu_front_average_root
        this.qingxu_centre_average_root = qingxu_centre_average_root
        this.qingxu_later_average_root = qingxu_later_average_root
        this.qingxu_effectiveness = qingxu_effectiveness
        this.pilao_front = pilao_front
        this.pilao_centre = pilao_centre
        this.pilao_later = pilao_later
        this.pilao_front_average = pilao_front_average
        this.pilao_centre_average = pilao_centre_average
        this.pilao_later_average = pilao_later_average
        this.pilao_front_average_root = pilao_front_average_root
        this.pilao_centre_average_root = pilao_centre_average_root
        this.pilao_later_average_root = pilao_later_average_root
        this.pilao_effectiveness = pilao_effectiveness
    }
}