package com.example.psychology.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.blankj.utilcode.util.SDCardUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.utils.FileUtils
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.databinding.FragmentMeditationBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.VideoData
import com.example.psychology.ui.my.MyActivity
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.DepthPageTransformer

/**
 * 冥想
 */
class MeditationFragment :
    BaseMVVMFragment<FragmentMeditationBinding, BaseViewModel>(R.layout.fragment_meditation) {

    private var meditationAdapter: MeditationAdapter? = null
    private val list = mutableListOf<String>()


    override fun initObserver() {
    }

    override fun initData() {
        applyDebouncingClickListener(
            bind.ivMeditationFavorite,
            bind.ivMeditationHistory
        )
    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when(v){
            bind.ivMeditationFavorite->{
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type",1))
            }
            bind.ivMeditationHistory->{
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type",0))
            }
        }
    }

    private var searchAdapter: SearchAdapter? = null
    private val searchList = mutableListOf<VideoData>()
    val testList = mutableListOf<String>()

    override fun initView() {
        testList.clear()
        testList.add("专注冥想.png")
        testList.add("内观冥想.png")
        testList.add("正念.png")

        bind.brMeditation.setAdapter(object : BannerImageAdapter<String>(testList) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: String?,
                position: Int,
                size: Int
            ) {
                val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/冥想/${data}"
                //图片加载自己实现
                Glide.with(holder!!.itemView)
                    .setDefaultRequestOptions(
                        RequestOptions()
                            .frame(1000000)
                            .centerInside()
                    )
                    .load(video_path)
                    .into(holder.imageView)

            }
        })
            .addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(activity))
            .addPageTransformer(DepthPageTransformer())
            .setBannerRound(20f)


        bind.rvMeditation.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        meditationAdapter =MeditationAdapter(mutableListOf())
        bind.rvMeditation.adapter = meditationAdapter
        list.clear()
        list.add("正念冥想")
        list.add("专注冥想")
        list.add("内观冥想")
        list.add("慈悲冥想")
        meditationAdapter?.setList(list)

//        bind.rvMeditation.addItemDecoration(SpacesItemDecoration(10))


        bind.rvRelaxTop2.layoutManager = GridLayoutManager(activity, 1)
        searchAdapter = SearchAdapter(mutableListOf())
        bind.rvRelaxTop2.adapter = searchAdapter
        bind.rvRelaxTop2.addItemDecoration(SpacesItemDecoration(10))


        searchAdapter?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", searchList[position].id)
            bundle.putString("video_path", searchList[position].video_path)
            Navigation.findNavController(v)
                .navigate(R.id.action_MeditationFragment_to_trainingFragment2, bundle)
            bind.etMeditationSearch.setText("")
        }
        bind.etMeditationSearch.addTextChangedListener {
            it.toString().loge()
            if (it.toString() == "") {
                bind.rlRelaxTop2.visibility = View.GONE
                return@addTextChangedListener
            }
            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val videoDataDao = db.videoDataDao()
                val findBySearch = videoDataDao.findBySearch(it.toString())
                searchList.clear()
                searchList.addAll(findBySearch)
                activity?.runOnUiThread {
                    if (searchList.isNotEmpty()) {
                        bind.rlRelaxTop2.visibility = View.VISIBLE
                    } else {
                        bind.rlRelaxTop2.visibility = View.GONE
                    }
                    searchAdapter?.setList(searchList)
                }
            }.start()

        }


    }

    override fun initVariableId() = BR.meditation


    inner class MeditationAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_adapter_meditation_list, data) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_adapter1_meditation_title,item)

            val rvMeditationItem = holder.getView<RecyclerView>(R.id.rvMeditationItem)
            rvMeditationItem.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            val meditationItemAdapter = MeditationItemAdapter(mutableListOf())
            rvMeditationItem.adapter=meditationItemAdapter
            rvMeditationItem.addItemDecoration(SpacesItemDecoration(10))
            val list2 = mutableListOf<VideoData>()

            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                list2.clear()
                val videoDataDao = db.videoDataDao()
                val loadAllByType = videoDataDao.loadAllByType(item, 1, 2)
                list2.addAll(loadAllByType)
                activity?.runOnUiThread {
                    meditationItemAdapter.setList(list2)
                }
            }.start()

            meditationItemAdapter.setOnItemClickListener{_,v,position->
                val bundle = Bundle()
                bundle.putLong("id",list2[position].id)
                bundle.putString("video_path", list2[position].video_path)
                Navigation.findNavController(v).navigate(R.id.action_MeditationFragment_to_trainingFragment2,bundle)
            }

            meditationItemAdapter.setOnItemLongClickListener { adapter, view, position ->
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("提示")
                builder.setMessage("是否删除此视频")
                builder.setPositiveButton("确定") { dialog, which ->
                    val db = Room.databaseBuilder(
                        requireActivity(),
                        AppDatabase::class.java, "users_dp"
                    ).build()
                    Thread {
                        val videoDataDao = db.videoDataDao()
                        videoDataDao.delete(list2[position])
                        list2.removeAt(position)
                        activity?.runOnUiThread {
                            meditationItemAdapter.setList(list2)
                        }
                    }.start()
                }
                builder.setNegativeButton("取消") { dialog, which ->

                }
                val dialog = builder.create()
                dialog.show()
                return@setOnItemLongClickListener true
            }

        }

    }

    inner class MeditationItemAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_meditation_list_item, data) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {
            holder.setText(R.id.tv_adapter_meditation_title,item.video_title)
                .setText(R.id.tv_adapter_meditation_content,item.video_content)
            val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/img/${item.video_title}.png"
            Glide.with(activity!!)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerInside()
                )
                .load(video_path)
                .into(holder.getView(R.id.iv_adapter_meditation))
        }

    }


    inner class SearchAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_relax_search) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {
            holder.setText(R.id.tv_relax_search_title, item.video_title)
        }

    }
}