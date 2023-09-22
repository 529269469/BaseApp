package com.example.psychology.ui.my

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
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
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.databinding.FragmentMyListBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.CalendarData
import com.example.psychology.ui.LoginActivity.Companion.uid
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar


class MyListFragment :
    BaseMVVMFragment<FragmentMyListBinding, BaseViewModel>(R.layout.fragment_my_list) {
    override fun initObserver() {
    }


    var year = 0
    var month = 0
    var day = 0

    var catalogue = 1
    override fun initData() {
        catalogue = arguments?.getInt("catalogue") ?: 1
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        val calendarDao = db.calendarDao()

        val nowDate = TimeUtils.getNowDate()
        val calendar = Calendar.getInstance()
        calendar.time = nowDate
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
        if (catalogue == 1) {
            bind.tvMyHistory1.visibility = View.VISIBLE
            bind.tvMyHistory2.visibility = View.VISIBLE
            bind.tvMyHistory3.visibility = View.VISIBLE
            Thread {
                val dayNum = year * 24 + month * 31 + day

                val listAll =
                    calendarDao.findByDay(dayNum, catalogue, uid!!).sortedByDescending { it.id }
                        .distinctBy { it.video_id }

                val listAll2 =
                    calendarDao.findByDay(dayNum - 1, catalogue, uid!!).sortedByDescending { it.id }
                        .distinctBy { it.video_id }

                val findByDays =
                    calendarDao.findByDays(dayNum - 2, catalogue, uid!!).sortedByDescending { it.id }
                        .distinctBy { it.video_id }

                activity?.runOnUiThread {
                    list.clear()
                    list.addAll(listAll)
                    if (list.isNotEmpty()) {
                        bind.llMyList.visibility = View.VISIBLE
                        bind.ivMyListNo.visibility = View.GONE
                    }
                    myListAdapter?.setList(list)

                    list2.clear()
                    list2.addAll(listAll2)
                    if (list2.isNotEmpty()) {
                        bind.llMyList.visibility = View.VISIBLE
                        bind.ivMyListNo.visibility = View.GONE
                    }
                    myListAdapter2?.setList(list2)

                    list3.clear()
                    list3.addAll(findByDays)
                    if (list3.isNotEmpty()) {
                        bind.llMyList.visibility = View.VISIBLE
                        bind.ivMyListNo.visibility = View.GONE
                    }
                    myListAdapter3?.setList(list3)

                }

            }.start()
        } else {
            bind.tvMyHistory1.visibility = View.GONE
            bind.tvMyHistory2.visibility = View.GONE
            bind.tvMyHistory3.visibility = View.GONE
            Thread {
                val loadAllByIds = calendarDao.findByCatalogue(2, uid!!)
                activity?.runOnUiThread {
                    list.clear()
                    list.addAll(loadAllByIds)
                    if (list.isNotEmpty()) {
                        bind.llMyList.visibility = View.VISIBLE
                        bind.ivMyListNo.visibility = View.GONE
                    }
                    myListAdapter?.setList(list)
                }
            }.start()
        }
    }

    override fun initView() {
        initClick()
    }

    private var myListAdapter: MyListAdapter? = null
    private val list = mutableListOf<CalendarData>()

    private var myListAdapter2: MyListAdapter? = null
    private val list2 = mutableListOf<CalendarData>()

    private var myListAdapter3: MyListAdapter? = null
    private val list3 = mutableListOf<CalendarData>()
    private fun initClick() {
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        //全部
        bind.tvMyList1.setOnClickListener {
            bind.tvMyList1.setBackgroundResource(R.drawable.shape_report_tab)
            bind.tvMyList1.setTextColor(activity?.resources!!.getColor(R.color.white, null))
            bind.tvMyList2.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList2.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            bind.tvMyList3.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList3.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            val calendarDao = db.calendarDao()

            if (catalogue == 1) {
                Thread {
                    val dayNum = year * 24 + month * 31 + day

                    val listAll =
                        calendarDao.findByDay(dayNum, catalogue, uid!!).sortedByDescending { it.id }
                            .distinctBy { it.video_id }

                    val listAll2 = calendarDao.findByDay(dayNum - 1, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    val findByDays = calendarDao.findByDays(dayNum - 2, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(listAll)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)
                        list2.clear()
                        list2.addAll(listAll2)
                        if (list2.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter2?.setList(list2)
                        list3.clear()
                        list3.addAll(findByDays)
                        if (list3.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter3?.setList(list3)
                    }
                }.start()
            } else {
                Thread {
                    val loadAllByIds = calendarDao.findByCatalogue(2, uid!!)
                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(loadAllByIds)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)
                    }
                }.start()
            }


        }
        //放松
        bind.tvMyList2.setOnClickListener {
            bind.tvMyList2.setBackgroundResource(R.drawable.shape_report_tab)
            bind.tvMyList2.setTextColor(activity?.resources!!.getColor(R.color.white, null))
            bind.tvMyList1.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList1.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            bind.tvMyList3.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList3.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            val calendarDao = db.calendarDao()

            if (catalogue == 1) {
                Thread {
                    val dayNum = year * 24 + month * 31 + day


                    val listF = calendarDao.findByDay(dayNum, 1, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    val listF2 = calendarDao.findByDay(dayNum - 1, 1, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    val findByDays = calendarDao.findByDays(dayNum - 2, 1, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(listF)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)

                        list2.clear()
                        list2.addAll(listF2)
                        if (list2.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter2?.setList(list2)

                        list3.clear()
                        list3.addAll(findByDays)
                        if (list3.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter3?.setList(list3)


                    }
                }.start()
            } else {
                Thread {
                    val loadAllByIds = calendarDao.findByCatalogue(2, uid!!, 1)
                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(loadAllByIds)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)

                    }
                }.start()
            }

        }
        //冥想
        bind.tvMyList3.setOnClickListener {
            bind.tvMyList3.setBackgroundResource(R.drawable.shape_report_tab)
            bind.tvMyList3.setTextColor(activity?.resources!!.getColor(R.color.white, null))
            bind.tvMyList1.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList1.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            bind.tvMyList2.setBackgroundResource(R.drawable.shape_login_white_20)
            bind.tvMyList2.setTextColor(activity?.resources!!.getColor(R.color.black, null))
            val calendarDao = db.calendarDao()

            if (catalogue == 1) {
                Thread {
                    val dayNum = year * 24 + month * 31 + day

                    val listF = calendarDao.findByDay(dayNum, 2, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    val listF2 = calendarDao.findByDay(dayNum - 1, 2, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    val findByDays = calendarDao.findByDays(dayNum - 2, 2, catalogue, uid!!)
                        .sortedByDescending { it.id }.distinctBy { it.video_id }

                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(listF)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)

                        list2.clear()
                        list2.addAll(listF2)
                        if (list2.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter2?.setList(list2)

                        list3.clear()
                        list3.addAll(findByDays)
                        if (list3.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter3?.setList(list3)

                    }
                }.start()
            } else {
                Thread {
                    val loadAllByIds = calendarDao.findByCatalogue(2, uid!!, 2)
                    activity?.runOnUiThread {
                        list.clear()
                        list.addAll(loadAllByIds)
                        if (list.isNotEmpty()) {
                            bind.llMyList.visibility = View.VISIBLE
                            bind.ivMyListNo.visibility = View.GONE
                        }
                        myListAdapter?.setList(list)

                    }
                }.start()
            }
        }

        bind.rvMyHistory.layoutManager = GridLayoutManager(activity, 5)
        myListAdapter = MyListAdapter(mutableListOf())
        bind.rvMyHistory.adapter = myListAdapter
        bind.rvMyHistory.addItemDecoration(SpacesItemDecoration(16))

        bind.rvMyHistory2.layoutManager = GridLayoutManager(activity, 5)
        myListAdapter2 = MyListAdapter(mutableListOf())
        bind.rvMyHistory2.adapter = myListAdapter2
        bind.rvMyHistory2.addItemDecoration(SpacesItemDecoration(16))

        bind.rvMyHistory3.layoutManager = GridLayoutManager(activity, 5)
        myListAdapter3 = MyListAdapter(mutableListOf())
        bind.rvMyHistory3.adapter = myListAdapter3
        bind.rvMyHistory3.addItemDecoration(SpacesItemDecoration(16))

        myListAdapter?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", list[position].video_id!!)
            bundle.putString("video_path", list[position].path)
            Navigation.findNavController(v)
                .navigate(R.id.action_RelaxFragment_to_trainingFragment2_2, bundle)
        }

        myListAdapter2?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", list2[position].video_id!!)
            bundle.putString("video_path", list2[position].path)
            Navigation.findNavController(v)
                .navigate(R.id.action_RelaxFragment_to_trainingFragment2_2, bundle)
        }

        myListAdapter3?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", list3[position].video_id!!)
            bundle.putString("video_path", list3[position].path)
            Navigation.findNavController(v)
                .navigate(R.id.action_RelaxFragment_to_trainingFragment2_2, bundle)
        }

    }

    inner class MyListAdapter(data: MutableList<CalendarData>) :
        BaseQuickAdapter<CalendarData, BaseViewHolder>(R.layout.layout_adapter_my_list_item, data) {
        override fun convert(holder: BaseViewHolder, item: CalendarData) {
            val format = SimpleDateFormat("mm:ss")
            holder.setText(R.id.tv_layout_adapter_my_list_item_title, item.title)
                .setText(
                    R.id.tv_layout_adapter_my_list_item_time,
                    if (item.time == 0) "已观看 00:01" else "已观看${format.format(item.time)}"
                )
            val video_path =
                SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/img/${item.title}.png"

            if (item.path?.substring(item.path!!.length - 3, item.path!!.length) == "mp3") {
                holder.setImageResource(R.id.iv_layout_adapter_my_list_item, R.mipmap.icon_audio)
            } else {
                Glide.with(activity!!)
                    .setDefaultRequestOptions(
                        RequestOptions()
                            .frame(1000000)
                            .centerInside()
                    )
                    .load(if (File(video_path).exists()) video_path else item.path)
                    .into(holder.getView(R.id.iv_layout_adapter_my_list_item))
            }
        }

    }

    override fun initVariableId() = BR.my_list

}