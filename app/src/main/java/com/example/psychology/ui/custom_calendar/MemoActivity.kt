package com.example.psychology.ui.custom_calendar

import androidx.room.Room
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.databinding.ActivityMemoBinding
import com.example.psychology.romm.base.AppDatabase

/**
 * 备忘录
 */
class MemoActivity : BaseMVVMActivity<ActivityMemoBinding,BaseViewModel>(R.layout.activity_memo) {


    override fun initObserver() {
    }

    override fun initData() {

    }

    override fun initView() {
        val newMonth=intent.getStringExtra("newMonth")
        val day=intent.getStringExtra("day")
        val user_name=intent.getStringExtra("user_name").toString()

        bind.tvMemoTitle.text = "$newMonth - $day   历史训练"
        bind.ivMemoLeft.setOnClickListener { finish() }


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "users_dp"
        ).build()
        val calendarDao = db.calendarDao()

//        Thread{
//            val findByMonth =
//                calendarDao.findByDay(newMonth!!, day!!,user_name)
//            var str=StringBuffer()
//            findByMonth.forEach {
//                str.append(it.time).append(" : ").append(it.content).append("\n")
//            }
//
//            runOnUiThread {
//                bind.tvMemo.text=str.toString()
//            }
//
//        }.start()

    }

    override fun initVariableId()=BR.memo
}