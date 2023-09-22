package com.example.psychology.ui.custom_calendar

import android.content.Intent
import androidx.room.Room
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMActivity
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.databinding.ActivityCustomCalendarBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.ui.custom_calendar.CustomCalendarView.OnMonthChangerListener

/**
 * 日历
 */
class CustomCalendarActivity :
    BaseMVVMActivity<ActivityCustomCalendarBinding, BaseViewModel>(R.layout.activity_custom_calendar) {

    override fun initObserver() {
    }

    override fun initData() {
    }

    override fun initView() {
        val user_name = intent.getStringExtra("user_name").toString()

        val month: String = bind.calendarView.month
        bind.tvDate.text = month

        bind.ivLastMonth.setOnClickListener { //上一个月
            bind.calendarView.goLastMonth()
        }
        bind.ivNextMonth.setOnClickListener { //下一个月
            bind.calendarView.goNextMonth()
        }

        bind.calendarView.setOnMonthChangerListener(object : OnMonthChangerListener {
            override fun onMonthChanger(lastMonth: String, newMonth: String) {
                bind.tvDate.text = newMonth
            }

            override fun onDay(day: String) {
                val newMonth = bind.tvDate.text.toString()
                val day = day
                startActivity(
                    Intent(this@CustomCalendarActivity, MemoActivity::class.java)
                        .putExtra("newMonth", newMonth)
                        .putExtra("day", day)
                        .putExtra("user_name", user_name)
                )
            }
        })
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "users_dp"
        ).build()
        val calendarDao = db.calendarDao()



    }

    override fun initVariableId() = BR.custom_calendar
}