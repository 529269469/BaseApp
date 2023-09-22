package com.example.psychology.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.databinding.FragmentReportBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.dao.CalendarDao
import com.example.psychology.romm.entity.ReportData
import com.example.psychology.ui.LoginActivity.Companion.uid
import com.example.psychology.ui.LoginActivity.Companion.user_name
import com.example.psychology.ui.my.MyActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar


class ReportFragment :
    BaseMVVMFragment<FragmentReportBinding, BaseViewModel>(R.layout.fragment_report) {
    override fun initObserver() {
    }

    //    val timeWeek = arrayOf("日", "一", "二", "三", "四", "五", "六")
    var calendar: Calendar? = null
    var year = 0
    var month = 0
    var day = 0

    val weekNum= arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    val mouthNum = arrayOf(
        "1月",
        "2月",
        "3月",
        "4月",
        "5月",
        "6月",
        "7月",
        "8月",
        "9月",
        "10月",
        "11月",
        "12月"
    )

    @SuppressLint("NewApi")
    override fun initData() {
        list.clear()
        list2.clear()
        list3.clear()

        val nowDate = TimeUtils.getNowDate()
        calendar = Calendar.getInstance()
        calendar?.time = nowDate

        year = calendar?.get(Calendar.YEAR)!!
        month = calendar?.get(Calendar.MONTH)!!
        day = calendar?.get(Calendar.DAY_OF_MONTH)!!
        val monthDay = calendar?.getActualMaximum(Calendar.DAY_OF_MONTH)

        bind.tvReportYear.text = "$year-${month!!+1}"

        for (i in 0 until  monthDay!!) {
            val calendarThem = Calendar.getInstance()
            calendarThem.set(year, month, i)
            val getWeek = calendarThem.get(Calendar.DAY_OF_WEEK)
            if (day-1  == i) {
                list.add(TimeBean(weekNum[getWeek-1], i+1, true))
            } else {
                list.add(TimeBean(weekNum[getWeek-1], i+1, false))
            }
        }
        timeAdapter?.setList(list)
        bind.rvReportTime.scrollToPosition(day-1)
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()

        list2.clear()
        list21.clear()
        Thread {
            val calendarDao = db.calendarDao()
            val today = LocalDate.now()
            val startOfWeek =
                today.minusDays(today.dayOfWeek.value.toLong() - DayOfWeek.MONDAY.value.toLong())
            list2.add(TimeBean(weekNum[0], getweekNum(startOfWeek, 0, calendarDao), false))
            list2.add(TimeBean(weekNum[1], getweekNum(startOfWeek, 1, calendarDao), false))
            list2.add(TimeBean(weekNum[2], getweekNum(startOfWeek, 2, calendarDao), false))
            list2.add(TimeBean(weekNum[3], getweekNum(startOfWeek, 3, calendarDao), false))
            list2.add(TimeBean(weekNum[4], getweekNum(startOfWeek, 4, calendarDao), false))
            list2.add(TimeBean(weekNum[5], getweekNum(startOfWeek, 5, calendarDao), false))
            list2.add(TimeBean(weekNum[6], getweekNum(startOfWeek, 6, calendarDao), false))

            list21.add(TimeBean(mouthNum[0], getMouthNum(calendarDao, 1), false))
            list21.add(TimeBean(mouthNum[1], getMouthNum(calendarDao, 2), false))
            list21.add(TimeBean(mouthNum[2], getMouthNum(calendarDao, 3), false))
            list21.add(TimeBean(mouthNum[3], getMouthNum(calendarDao, 4), false))
            list21.add(TimeBean(mouthNum[4], getMouthNum(calendarDao, 5), false))
            list21.add(TimeBean(mouthNum[5], getMouthNum(calendarDao, 6), false))
            list21.add(TimeBean(mouthNum[6], getMouthNum(calendarDao, 7), false))
            list21.add(TimeBean(mouthNum[7], getMouthNum(calendarDao, 8), false))
            list21.add(TimeBean(mouthNum[8], getMouthNum(calendarDao, 9), false))
            list21.add(TimeBean(mouthNum[9], getMouthNum(calendarDao, 10), false))
            list21.add(TimeBean(mouthNum[10], getMouthNum(calendarDao, 11), false))
            list21.add(TimeBean(mouthNum[11], getMouthNum(calendarDao, 12), false))

            activity?.runOnUiThread {
                timeAdapter2?.setList(list2)
                timeAdapter21?.setList(list21)

            }
        }.start()



        Thread {
            val reportDataDao = db.ReportDataDao()
            val userDao = db.userDao()
            val findById = reportDataDao.findById(year, month + 1, day, uid!!)
            list3.clear()
            list3.addAll(findById)

            val loadAllByUserId = reportDataDao.loadAllByUserId(uid!!)
            val user = userDao.findByName(user_name!!)
            activity?.runOnUiThread {
                bind.tvReportLeiji.text = loadAllByUserId.size.toString()
                bind.tvReportLianxu.text = user.continuous_time.toString()
                bind.tvReportLeijiDay.text = user.add_up_time.toString()

                if (list3.isEmpty()) {
                    reportAdapter?.setList(list3)
                    bind.rlReportNot.visibility = View.VISIBLE
                    bind.llReport2.visibility = View.GONE
                } else {
                    bind.rlReportNot.visibility = View.GONE
                    bind.llReport2.visibility = View.VISIBLE
                    reportAdapter?.setList(list3)

                }
            }


        }.start()


    }


    private var timeAdapter: TimeAdapter? = null
    private val list = mutableListOf<TimeBean>()

    private var timeAdapter2: TimeAdapter2? = null
    private val list2 = mutableListOf<TimeBean>()
    private var timeAdapter21: TimeAdapter2? = null
    private val list21 = mutableListOf<TimeBean>()

    private var reportAdapter: ReportAdapter? = null
    private val list3 = mutableListOf<ReportData>()
    override fun initView() {
        applyDebouncingClickListener(
            bind.ivReportFavorite,
            bind.ivReportHistory,
            bind.ivReportWeekMouth,
            bind.tvReportSelectYear
        )

        bind.rvReportTime.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        timeAdapter = TimeAdapter(mutableListOf())
        bind.rvReportTime.addItemDecoration(SpacesItemDecoration(12))
        bind.rvReportTime.adapter = timeAdapter


        timeAdapter?.setOnItemClickListener { _, _, position ->
            list.forEach {
                it.isTime = false
            }
            list[position].isTime = true
            timeAdapter?.setList(list)
            day = list[position].time2
            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val reportDataDao = db.ReportDataDao()
                val findById = reportDataDao.findById(year, month + 1, day, uid!!)
                list3.clear()
                list3.addAll(findById)
                activity?.runOnUiThread {
                    if (list3.isEmpty()) {
                        reportAdapter?.setList(list3)
                        bind.rlReportNot.visibility = View.VISIBLE
                        bind.llReport2.visibility = View.GONE
                    } else {
                        bind.rlReportNot.visibility = View.GONE
                        bind.llReport2.visibility = View.VISIBLE
                        reportAdapter?.setList(list3)

                    }

                }
            }.start()

        }


        bind.rvReportTime2.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        timeAdapter2 = TimeAdapter2(mutableListOf())
        bind.rvReportTime2.addItemDecoration(SpacesItemDecoration(27))
        bind.rvReportTime2.adapter = timeAdapter2

        bind.rvReportTime3.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        timeAdapter21 = TimeAdapter2(mutableListOf())
        bind.rvReportTime3.addItemDecoration(SpacesItemDecoration(27))
        bind.rvReportTime3.adapter = timeAdapter21


        bind.rvReport.layoutManager = GridLayoutManager(activity, 1)
        reportAdapter = ReportAdapter(mutableListOf())
        bind.rvReport.addItemDecoration(SpacesItemDecoration(8))
        bind.rvReport.adapter = reportAdapter


        reportAdapter?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", list3[position].id!!)
            Navigation.findNavController(v)
                .navigate(R.id.action_BreatheFragment_to_reportDetailsFragment, bundle)
        }

        bind.ivReportTop.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            val monthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            bind.tvReportYear.text = "$year-${month + 1}"
            list.clear()
            for (i in 0 until monthDay) {
                val calendarThem = Calendar.getInstance()
                calendarThem.set(year, month, i)
                val getWeek = calendarThem.get(Calendar.DAY_OF_WEEK)
                list.add(TimeBean(weekNum[getWeek - 1], i+1, false))
            }
            timeAdapter?.setList(list)
            bind.rvReportTime.scrollToPosition(0)
        }

        bind.ivReportNet.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(year, month + 1, day)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            val monthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            bind.tvReportYear.text = "$year-${month+1}"
            list.clear()
            for (i in 0 until monthDay) {
                val calendarThem = Calendar.getInstance()
                calendarThem.set(year, month, i)
                val getWeek = calendarThem.get(Calendar.DAY_OF_WEEK)
                list.add(TimeBean(weekNum[getWeek - 1], i+1, false))
            }
            timeAdapter?.setList(list)
            bind.rvReportTime.scrollToPosition(0)
        }
    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when (v) {
            bind.ivReportFavorite -> {
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type", 1))
            }

            bind.ivReportHistory -> {
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type", 0))
            }

            bind.ivReportWeekMouth -> {
                val selectList = mutableListOf<String>()
                selectList.add("周数")
                selectList.add("月数")
                selectMouthOrWeek(bind.ivReportWeekMouth, selectList)

            }

            bind.tvReportSelectYear -> {
                val selectList = mutableListOf<Int>()
                selectList.add(2023)
                selectList.add(2024)
                selectList.add(2025)
                selectList.add(2026)
                selectList.add(2027)
                selectList.add(2028)
                selectList.add(2029)
                selectList.add(2030)
                selectList.add(2031)
                selectList.add(2032)
                selectList.add(2033)
                selectMouthOrYear(bind.tvReportSelectYear, selectList)
            }
        }
    }

    private var selectWeekWindow: PopupWindow? = null

    @SuppressLint("NewApi")
    fun selectMouthOrWeek(view: View, mutableList: MutableList<String>) {
        selectWeekWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_select, null)
        selectWeekWindow?.contentView = inflate
        selectWeekWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        selectWeekWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        selectWeekWindow?.setBackgroundDrawable(ColorDrawable(0))
        selectWeekWindow?.isFocusable = true
        selectWeekWindow?.isOutsideTouchable = true

        selectWeekWindow?.showAsDropDown(view)
        val rv_layout_popup_select = inflate.findViewById<RecyclerView>(R.id.rv_layout_popup_select)
        rv_layout_popup_select.layoutManager = GridLayoutManager(activity, 1)
        var selectAdapter = SelectWeekAdapter(mutableList)
        rv_layout_popup_select.addItemDecoration(SpacesItemDecoration(2))
        rv_layout_popup_select.adapter = selectAdapter
        selectAdapter.setOnItemClickListener { _, v, position ->
            selectWeekWindow?.dismiss()
            if (position == 0) {
                bind.rvReportTime2.visibility = View.VISIBLE
                bind.rvReportTime3.visibility = View.GONE
                bind.tvReportRank.text = "注:数据以每天的训练时长(0min-100min)为准"
                bind.ivReportWeekMouth.setImageResource(R.mipmap.dropdown)
            } else {
                bind.tvReportRank.text = "注:数据以每月的训练时长为准(0h-50h)为准"
                bind.ivReportWeekMouth.setImageResource(R.mipmap.icon_mouth)
                bind.rvReportTime2.visibility = View.GONE
                bind.rvReportTime3.visibility = View.VISIBLE

            }

        }
    }

    private fun getMouthNum(calendarDao: CalendarDao, mouth: Int): Int {
        val findByMonth = calendarDao.findByMonth("$year", "$mouth", 1, uid!!)
        var timeWeek = 0
        findByMonth.forEach {
            timeWeek += it.time!!
        }
        return timeWeek / 1000 / 60 / 60
    }

    @SuppressLint("NewApi")
    private fun getweekNum(startWeek: LocalDate?, i: Long, calendarDao: CalendarDao): Int {
        val startOfWeek = startWeek?.plusDays(i-1)
        val split = startOfWeek.toString().split("-")
        val findById = calendarDao.findByDay(
            split[0],
            split[1].toInt().toString(),
            split[2].toInt().toString(),
            1,
            uid!!
        )
        var timeWeek = 0
        findById.forEach {
            timeWeek += it.time!!
        }
        return timeWeek / 1000 / 60
    }


    private var selectWindow: PopupWindow? = null
    fun selectMouthOrYear(view: View, mutableList: MutableList<Int>) {
        selectWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_select, null)
        selectWindow?.contentView = inflate
        selectWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        selectWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        selectWindow?.setBackgroundDrawable(ColorDrawable(0))
        selectWindow?.isFocusable = true
        selectWindow?.isOutsideTouchable = true

        selectWindow?.showAsDropDown(view)

        val rv_layout_popup_select = inflate.findViewById<RecyclerView>(R.id.rv_layout_popup_select)

        rv_layout_popup_select.layoutManager = GridLayoutManager(activity, 1)
        var selectAdapter = SelectAdapter(mutableList)
        rv_layout_popup_select.addItemDecoration(SpacesItemDecoration(2))
        rv_layout_popup_select.adapter = selectAdapter

        selectAdapter.setOnItemClickListener { _, v, position ->
            year = mutableList[position]
            calendar?.set(year, month, day)
            year = calendar?.get(Calendar.YEAR)!!
            month = calendar?.get(Calendar.MONTH)!!
            val monthDay = calendar?.getActualMaximum(Calendar.DAY_OF_MONTH)
            bind.tvReportYear.text = "$year-${month + 1}"
            list.clear()
            for (i in 1..monthDay!!) {
                val calendarThem = Calendar.getInstance()
                calendarThem.set(year, month, i)
                val getWeek = calendarThem.get(Calendar.DAY_OF_WEEK)
                list.add(TimeBean(weekNum[getWeek - 1], i, false))
            }
            timeAdapter?.setList(list)
            bind.rvReportTime.scrollToPosition(0)

            list3.clear()
            reportAdapter?.setList(list3)
            selectWindow?.dismiss()
        }

    }

    inner class SelectWeekAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_popup_select_list, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_layout_popup_select_list, item)

        }
    }

    inner class SelectAdapter(data: MutableList<Int>) :
        BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_popup_select_list, data) {
        override fun convert(holder: BaseViewHolder, item: Int) {
            holder.setText(R.id.tv_layout_popup_select_list, item.toString())

        }
    }


    override fun initVariableId() = BR.report

    inner class ReportAdapter(data: MutableList<ReportData>) :
        BaseQuickAdapter<ReportData, BaseViewHolder>(R.layout.layout_adapter_report_list, data) {
        override fun convert(holder: BaseViewHolder, item: ReportData) {
            holder.setText(
                R.id.tv_layout_adapter_report_list_title,
                "放松冥想身心反馈报告(${holder.layoutPosition + 1})"
            ).setText(R.id.tv_layout_adapter_report_list_time,TimeUtils.millis2String(item.id!!).substring(11,TimeUtils.millis2String(item.id!!).length))

        }
    }


    inner class TimeAdapter(data: MutableList<TimeBean>) :
        BaseQuickAdapter<TimeBean, BaseViewHolder>(R.layout.layout_adapter_time_list, data) {
        override fun convert(holder: BaseViewHolder, item: TimeBean) {
            holder.setText(R.id.tv_time_week, item.time1)
                .setTextColor(
                    R.id.tv_time_week, if (item.isTime) resources.getColor(R.color.white)
                    else resources.getColor(R.color.color_7B6F72)
                )
                .setText(R.id.tv_time_calendar, item.time2.toString())
                .setTextColor(
                    R.id.tv_time_calendar, if (item.isTime) resources.getColor(R.color.white)
                    else resources.getColor(R.color.color_7B6F72)
                )
                .setBackgroundResource(
                    R.id.ll_time, if (item.isTime) R.drawable.shape_jianbian_tiem_10
                    else R.drawable.shape_time_10
                )

        }
    }

    inner class TimeAdapter2(data: MutableList<TimeBean>) :
        BaseQuickAdapter<TimeBean, BaseViewHolder>(R.layout.layout_adapter_time2_list, data) {
        override fun convert(holder: BaseViewHolder, item: TimeBean) {
            holder.setText(R.id.tv_time2_title, item.time1)
                .getView<View>(R.id.v_time2_v).layoutParams.height =
                ConvertUtils.dp2px((item.time2) / 1.84f)

        }
    }


    data class TimeBean(
        var time1: String,
        var time2: Int,
        var isTime: Boolean
    )

}