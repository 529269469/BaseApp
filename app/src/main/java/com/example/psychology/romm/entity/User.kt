package com.example.psychology.romm.entity

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "users")
class User {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
    var user_name: String? = null//用户名
    var name: String? = null//昵称
    var password: String? = null//密码
    var head: Int? = null//头像
    var signature: String? = null//签名
    var isAdmin:Boolean?=false//是否时管理员

    var update_time:Long?=null //上次登录时间
    var add_up_time:Int?=null //累计
    var continuous_time:Int?=null //连续
    var time:Int?=null

    constructor(
        user_name: String?,
        name: String?,
        password: String?,
        head: Int?,
        signature: String?,
        isAdmin: Boolean?,
        update_time: Long?,
        add_up_time: Int?,
        continuous_time: Int?,
        time: Int?
    ) {
        this.user_name = user_name
        this.name = name
        this.password = password
        this.head = head
        this.signature = signature
        this.isAdmin = isAdmin
        this.update_time = update_time
        this.add_up_time = add_up_time
        this.continuous_time = continuous_time
        this.time = time
    }
}

