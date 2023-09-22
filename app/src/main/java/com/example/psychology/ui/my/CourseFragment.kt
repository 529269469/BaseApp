package com.example.psychology.ui.my

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
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
import com.example.psychology.databinding.FragmentCourseBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.VideoData
import com.leon.lfilepickerlibrary.LFilePicker
import java.io.File


/**
 * 我的课程
 */
class CourseFragment :
    BaseMVVMFragment<FragmentCourseBinding, BaseViewModel>(R.layout.fragment_course) {
    override fun initObserver() {
    }

    override fun initData() {
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        Thread {
            list.clear()
            val videoDataDao = db.videoDataDao()
            val loadAllByType = videoDataDao.loadAllCustom("我的课程",  1)
            list.addAll(loadAllByType)
            activity?.runOnUiThread {
                if (list.isEmpty()) {
                    bind.ivCourse.visibility = View.VISIBLE
                    bind.rvCourse.visibility = View.GONE
                } else {
                    list.add(VideoData())
                    bind.ivCourse.visibility = View.GONE
                    bind.rvCourse.visibility = View.VISIBLE
                }
                courseAdapter?.setList(list)
            }
        }.start()


    }
    val REQUESTCODE_FROM_ACTIVITY = 1000
    var courseAdapter: CourseAdapter? = null
    val list = mutableListOf<VideoData>()
    override fun initView() {

        bind.rvCourse.layoutManager = GridLayoutManager(activity, 5)
        courseAdapter = CourseAdapter(mutableListOf())
        bind.rvCourse.adapter = courseAdapter
        bind.rvCourse.addItemDecoration(SpacesItemDecoration(17))
        courseAdapter?.setList(list)

        bind.ivCourse.setOnClickListener {
            LFilePicker()
                .withSupportFragment(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withMutilyMode(false)
                .withChooseMode(true)
                .withFileFilter(arrayOf(".mp4",".avi",".wmv",".mp3"))
                .withStartPath("/storage/emulated/0/Movies") //指定初始显示路径
                .start()

        }

        courseAdapter?.setOnItemClickListener { _, v, position ->
            if (position==list.size-1){
                LFilePicker()
                    .withSupportFragment(this)
                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                    .withMutilyMode(false)
                    .withChooseMode(true)
                    .withFileFilter(arrayOf(".mp4",".avi",".wmv",".mp3"))
                    .withStartPath("/storage/emulated/0/Movies") //指定初始显示路径
                    .start()
            }else{
                val bundle = Bundle()
                bundle.putLong("id",list[position].id)
                Navigation.findNavController(v).navigate(R.id.action_courseFragment_to_trainingFragment2_2,bundle)
            }
        }
    }

    override fun initVariableId() = BR.course

    inner class CourseAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_course) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {

            if (holder.layoutPosition == list.size - 1) {
                holder.setImageResource(R.id.iv_adapter_course, R.mipmap.icon_add_video)
                    .setText(R.id.tv_adapter_course, "")
            }else{
                holder.setText(R.id.tv_adapter_course, item.video_title)
                Glide.with(activity!!)
                    .setDefaultRequestOptions(
                        RequestOptions()
                            .frame(1000000)
                            .centerInside()
                    )
                    .load(if (item.type==1) item.video_path else R.mipmap.icon_audio)
                    .into(holder.getView(R.id.iv_adapter_course))
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                val list: List<String>? = data?.getStringArrayListExtra("paths")
                val path = list!![0]
                val substring = path.substring(path.length - 3, path.length)
                val file = File(path)
                val video_title = file.name.substring(0, file.name.length - 4)
                val db = Room.databaseBuilder(
                    requireActivity(),
                    AppDatabase::class.java, "users_dp"
                ).build()
                val type = if (substring=="mp3") 2 else 1

                Thread{
                    val videoDataDao = db.videoDataDao()
                    videoDataDao.insertAll(
                        VideoData(
                            0,
                            video_title,
                            "",
                            path,
                            "我的课程",
                            type,
                            1,
                            0
                        )
                    )
                    activity?.runOnUiThread {
                        initData()
                    }
                }.start()


            }
        }

    }

}