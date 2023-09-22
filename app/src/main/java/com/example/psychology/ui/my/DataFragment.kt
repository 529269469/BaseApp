package com.example.psychology.ui.my

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.base_app.utils.SpacesItemDecoration2
import com.example.psychology.databinding.FragmentDataBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.dao.CalendarDao
import com.example.psychology.romm.entity.User
import com.example.psychology.romm.entity.VideoData
import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar


class DataFragment : BaseMVVMFragment<FragmentDataBinding, BaseViewModel>(R.layout.fragment_data) {
    override fun initObserver() {
    }



    @SuppressLint("NewApi", "SetTextI18n")
    override fun initData() {
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        val calendarDao = db.calendarDao()
        val today = LocalDate.now()
        val startOfWeek =
            today.minusDays(today.dayOfWeek.value.toLong() - DayOfWeek.MONDAY.value.toLong())


        Thread {
            val zNum1 = getweekNum(startOfWeek, 0, calendarDao, 1) + getweekNum(
                startOfWeek,
                1,
                calendarDao, 1
            ) + getweekNum(startOfWeek, 2, calendarDao, 1) + getweekNum(
                startOfWeek,
                3,
                calendarDao, 1
            ) + getweekNum(startOfWeek, 4, calendarDao, 1) + getweekNum(
                startOfWeek,
                5,
                calendarDao, 1
            ) + getweekNum(startOfWeek, 6, calendarDao, 1)

            val zNum2 = getweekNum(startOfWeek, 0, calendarDao, 2) + getweekNum(
                startOfWeek,
                1,
                calendarDao, 2
            ) + getweekNum(startOfWeek, 2, calendarDao, 2) + getweekNum(
                startOfWeek,
                3,
                calendarDao, 2
            ) + getweekNum(startOfWeek, 4, calendarDao, 2) + getweekNum(
                startOfWeek,
                5,
                calendarDao, 2
            ) + getweekNum(startOfWeek, 6, calendarDao, 2)

            val nowDate = TimeUtils.getNowDate()
            val calendar = Calendar.getInstance()
            calendar.time = nowDate
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val jNum1 = findByDay(1,year,month,day)
            val jNum2  = findByDay(2,year,month,day)

            val userDao = db.userDao()
            val userAll = userDao.getAll()
            listday.clear()
            list2.clear()
            list.clear()

            userAll.forEach {user->
                val userBean=user
                val sonWeek = getSonWeek(startOfWeek, calendarDao, userBean.uid)
                userBean.time=sonWeek
                list.add(userBean)
            }

            userAll.forEach {user->
                var userTime=0
                val findByCatalogueUser = calendarDao.findByDay("$year", "$month", "$day",1, user.uid)
                findByCatalogueUser.forEach { cata->
                    userTime += cata.time!!
                }
                user.time=userTime
                listday.add(user)
            }

            val listSorted = list.sortedByDescending{ it.time }
            val listDaySorted = listday.sortedByDescending { it.time }

            val videoDataDao = db.videoDataDao()
            val videoAll = videoDataDao.getAll()
            val videosortedBy = videoAll.sortedByDescending { it.num }

            list2.addAll(videosortedBy)
            activity?.runOnUiThread {
                bind.tvDataWeekTime1.text = "${zNum1 + zNum2}分钟"
                bind.tvDataWeekTime2.text = "$zNum1"
                bind.tvDataWeekTime3.text = "$zNum2"

                bind.tvDataDayTime.text = "${(jNum1 + jNum2) / 1000 / 60}分钟"
                bind.tvDataDayTime2.text = "${jNum1 / 1000 / 60}"
                bind.tvDataDayTime3.text = "${jNum2 / 1000 / 60}"

                rankingAdapter?.setList(list2)
                dataExcellentAdapter?.setList(listSorted)
                dataExcellentAdapter2?.setList(listDaySorted)
            }
        }.start()


    }

    private fun findByDay(i: Int, year: Int, month: Int, day: Int):Int {
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        val calendarDao = db.calendarDao()
        var jNum = 0
        val findById1 =
            calendarDao.findByDay("$year", "$month", "$day", 1, i)
        findById1.forEach {
            jNum += it.time!!
        }
        return jNum
    }

    @SuppressLint("NewApi")
    private fun getSonWeek(startWeek: LocalDate?,calendarDao: CalendarDao,userId:Long):Int{
        var timeWeek = 0
        for (i in 0..6){
            val startOfWeek = startWeek?.plusDays(i.toLong())
            val split = startOfWeek.toString().split("-")
            val findByCatalogueUser = calendarDao.findByDay( split[0],
                split[1].toInt().toString(),
                split[2].toInt().toString(),1, userId)
            findByCatalogueUser.forEach { cata->
                timeWeek += cata.time!!
            }
        }
        return timeWeek
    }

    @SuppressLint("NewApi")
    private fun getweekNum(
        startWeek: LocalDate?,
        i: Long,
        calendarDao: CalendarDao,
        type: Int
    ): Int {
        val startOfWeek = startWeek?.plusDays(i)
        val split = startOfWeek.toString().split("-")
        val findById =
            calendarDao.findByDay(
                split[0],
                split[1].toInt().toString(),
                split[2].toInt().toString(),
                1,
                type
            )
        var timeWeek = 0
        findById.forEach {
            timeWeek += it.time!!
        }
        return timeWeek / 1000 / 60
    }

    private var dataExcellentAdapter: DataExcellentAdapter? = null
    private val list = mutableListOf<User>()

    private var dataExcellentAdapter2: DataExcellentAdapter? = null
    private val listday = mutableListOf<User>()

    private var rankingAdapter: RankingAdapter? = null
    private val list2 = mutableListOf<VideoData>()
    override fun initView() {
        bind.rvDataWeekExcellent.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dataExcellentAdapter = DataExcellentAdapter(mutableListOf())
        bind.rvDataWeekExcellent.adapter = dataExcellentAdapter
        bind.rvDataWeekExcellent.addItemDecoration(SpacesItemDecoration2(-60))
        dataExcellentAdapter?.setOnItemClickListener { _, _, position ->
            excellentPopup(1)
        }
        bind.rlDataWeekExcellent.setOnClickListener {
            excellentPopup(1)
        }


        bind.rvDataDayExcellent.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dataExcellentAdapter2 = DataExcellentAdapter(mutableListOf())
        bind.rvDataDayExcellent.adapter = dataExcellentAdapter2

        bind.rvDataDayExcellent.addItemDecoration(SpacesItemDecoration2(-60))


        dataExcellentAdapter2?.setOnItemClickListener { _, _, position ->
            excellentPopup(2)
        }
        bind.rlDataDayExcellent.setOnClickListener {
            excellentPopup(2)
        }


        bind.rvDataRanking.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rankingAdapter = RankingAdapter(mutableListOf())
        bind.rvDataRanking.adapter = rankingAdapter



        rankingAdapter?.setList(list2)
        rankingAdapter?.setOnItemClickListener { _, _, position ->

        }


    }

    var excellentWindow: PopupWindow? = null

    /**
     * 优秀学员
     */
    @SuppressLint("MissingInflatedId")
    private fun excellentPopup(typeWeek:Int) {
        excellentWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_excellent, null)
        excellentWindow?.contentView = inflate
        excellentWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        excellentWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        excellentWindow?.setBackgroundDrawable(ColorDrawable(0))
        excellentWindow?.isFocusable = true
        excellentWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        excellentWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
        }
        excellentWindow?.showAtLocation(bind.rlDataWeekExcellent, Gravity.CENTER, 0, 0)

        inflate.findViewById<ImageView>(R.id.iv_excellent_popup_x)
            .setOnClickListener { excellentWindow?.dismiss() }
        val tv_excellent_popup_title = inflate.findViewById<TextView>(R.id.tv_excellent_popup_title)

        val rv_popup_adapter = inflate.findViewById<RecyclerView>(R.id.rv_popup_adapter)
        rv_popup_adapter.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val excellentAdapter = ExcellentAdapter(mutableListOf())
        rv_popup_adapter.adapter = excellentAdapter
        rv_popup_adapter.addItemDecoration(SpacesItemDecoration(15))
        val excellentList = mutableListOf<User>()
        when(typeWeek){
            1->{
                tv_excellent_popup_title.text="本周学员数据"
                val listSorted = list.sortedByDescending { it.time }
                excellentList.addAll(listSorted)
            }
            2->{
                tv_excellent_popup_title.text="今日学员数据"
                val listDaySorted = listday.sortedByDescending { it.time }
                excellentList.addAll(listDaySorted)
            }
        }


        excellentAdapter.setList(excellentList)

    }

    override fun initVariableId() = BR.data

    /**
     * 优秀学员
     */
    inner class ExcellentAdapter(data: MutableList<User>) :
        BaseQuickAdapter<User, BaseViewHolder>(R.layout.layout_adapter_data_excellent_item) {
        override fun convert(holder: BaseViewHolder, item: User) {


            holder.setImageResource(R.id.iv_adapter_data_excellent_head,item.head!!)
                .setText(R.id.tv_adapter_data_excellent_name,item.name)
                .setText(R.id.tv_adapter_data_excellent_tiem,"${item.time!!/1000/60}min")
            when (holder.layoutPosition) {
                0 -> {
                    holder.setBackgroundResource(
                        R.id.tv_adapter_data_excellent_num,
                        R.mipmap.first1
                    )
                }

                1 -> {
                    holder.setBackgroundResource(
                        R.id.tv_adapter_data_excellent_num,
                        R.mipmap.second2
                    )
                }

                2 -> {
                    holder.setBackgroundResource(
                        R.id.tv_adapter_data_excellent_num,
                        R.mipmap.third2
                    )
                }

                else -> {
                    holder.setBackgroundResource(
                        R.id.tv_adapter_data_excellent_num,
                        R.mipmap.rectangle3
                    )
                        .setText(R.id.tv_adapter_data_excellent_num, "${holder.layoutPosition + 1}")
                }
            }


        }

    }


    inner class DataExcellentAdapter(data: MutableList<User>) :
        BaseQuickAdapter<User, BaseViewHolder>(R.layout.layout_adapter_data_excellent, data) {
        override fun convert(holder: BaseViewHolder, item: User) {
                holder.setImageResource(R.id.iv_data_excellent,item.head!!)

        }

    }

    inner class RankingAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_data_ranking, data) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {
            val video_path =
                SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/img/${item.video_title}.png"
            Glide.with(activity!!)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerInside()
                )
                .load(if (item.type == 1) if (File(video_path).exists()) video_path else item.video_path else R.mipmap.icon_audio)
                .into(holder.getView(R.id.iv_data_ranking))

            holder.setText(R.id.tv_data_ranking_num, "${holder.layoutPosition + 1}")
                .setText(R.id.tv_data_ranking_look,"${item.num!!}")
                .setText(R.id.tv_data_ranking_title, item.video_title!!)
        }

    }


}