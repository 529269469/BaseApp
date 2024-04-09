package com.example.psychology.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
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
import com.example.psychology.databinding.FragmentRelaxBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.romm.entity.VideoData
import com.example.psychology.ui.my.MyActivity
import com.google.android.material.tabs.TabLayout
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.DepthPageTransformer
import java.io.File


/**
 * 放松
 */
class RelaxFragment :
    BaseMVVMFragment<FragmentRelaxBinding, BaseViewModel>(R.layout.fragment_relax) {

    private var relaxAdapter: RelaxAdapter? = null
    private val list = mutableListOf<VideoData>()

    private var searchAdapter: SearchAdapter? = null
    private val searchList = mutableListOf<VideoData>()

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
            val loadAllByType = videoDataDao.loadAllByType("音乐放松", 1, 1)
            list.addAll(loadAllByType)
            "initData ${loadAllByType.size}".loge()
            activity?.runOnUiThread {
                relaxAdapter?.setList(list)
            }
        }.start()

    }


    override fun initView() {
        val testList = mutableListOf<String>()
        testList.add("指导放松.png")
        testList.add("白噪音放松.png")
        testList.add("音乐放松.png")

        bind.brRelax.setAdapter(object : BannerImageAdapter<String>(testList) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: String?,
                position: Int,
                size: Int
            ) {
                val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/放松/${testList[position]}"
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


        bind.tbRelax.addTab(bind.tbRelax.newTab().setText("音乐放松"))
        bind.tbRelax.addTab(bind.tbRelax.newTab().setText("白噪音放松"))
        bind.tbRelax.addTab(bind.tbRelax.newTab().setText("指导放松"))
        bind.tbRelax.addTab(bind.tbRelax.newTab().setText("我的课程"))

        bind.tbRelax.tabRippleColor =
            ColorStateList.valueOf(activity?.resources!!.getColor(R.color.color_F9FBFB));

        bind.tbRelax.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text.toString().toString()
                val db = Room.databaseBuilder(
                    requireActivity(),
                    AppDatabase::class.java, "users_dp"
                ).build()
                Thread {
                    list.clear()
                    val videoDataDao = db.videoDataDao()
                    val loadAllByType = videoDataDao.loadAllByType(tab?.text.toString(), 1, 1)
                    list.addAll(loadAllByType)
                    activity?.runOnUiThread {
                        relaxAdapter?.setList(list)
                    }
                }.start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        bind.rvRelax.layoutManager = GridLayoutManager(activity, 1)
        relaxAdapter = RelaxAdapter(mutableListOf())
        bind.rvRelax.adapter = relaxAdapter


        relaxAdapter?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", list[position].id)
            bundle.putString("video_path", list[position].video_path)
            Navigation.findNavController(v)
                .navigate(R.id.action_RelaxFragment_to_trainingFragment2, bundle)
        }

        relaxAdapter?.setOnItemLongClickListener { adapter, view, position ->
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
                    videoDataDao.delete(list[position])
                    list.removeAt(position)
                    activity?.runOnUiThread {
                        relaxAdapter?.setList(list)
                    }
                }.start()
            }
            builder.setNegativeButton("取消") { dialog, which ->

            }
            val dialog = builder.create()
            dialog.show()
           return@setOnItemLongClickListener true
        }

        bind.rvRelax.addItemDecoration(SpacesItemDecoration(10))

        bind.rvRelaxTop2.layoutManager = GridLayoutManager(activity, 1)
        searchAdapter = SearchAdapter(mutableListOf())
        bind.rvRelaxTop2.adapter = searchAdapter
        bind.rvRelaxTop2.addItemDecoration(SpacesItemDecoration(10))


        searchAdapter?.setOnItemClickListener { _, v, position ->
            val bundle = Bundle()
            bundle.putLong("id", searchList[position].id)
            bundle.putString("video_path", searchList[position].video_path)
            Navigation.findNavController(v)
                .navigate(R.id.action_RelaxFragment_to_trainingFragment2, bundle)
            bind.etRelaxSearch.setText("")
        }
        bind.etRelaxSearch.addTextChangedListener {
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



        applyDebouncingClickListener(
            bind.ivRelaxCerebrum,
            bind.ivRelaxLung,
            bind.tvRelaxSearch,
            bind.ivRelaxFavorite,
            bind.ivRelaxHistory
        )


    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when (v) {
            bind.ivRelaxCerebrum -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_RelaxFragment_to_brainWaveFragment)
            }

            bind.ivRelaxLung -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_RelaxFragment_to_breatheFragment)
            }

            bind.tvRelaxSearch -> {
                if (bind.rlRelaxTop2.visibility == View.GONE) {
                    bind.rlRelaxTop2.visibility = View.VISIBLE
                } else {
                    bind.rlRelaxTop2.visibility = View.GONE
                }
            }

            bind.ivRelaxFavorite -> {
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type", 1))
            }

            bind.ivRelaxHistory -> {
                startActivity(Intent(activity, MyActivity::class.java).putExtra("type", 0))
            }
        }

    }

    override fun initVariableId() = BR.relax

    inner class RelaxAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_relax_list, data) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {
            holder.setText(R.id.tv_adapter_relax_title, item.video_title)
                .setText(R.id.tv_adapter_relax_content, item.video_content)
            val video_path = SDCardUtils.getSDCardPathByEnvironment() + "/Movies/生理反馈/img/${item.video_title}.png"
            Glide.with(activity!!)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerInside()
                )
                .load(if (item.type==1) if (File(video_path).exists()) video_path else item.video_path else R.mipmap.icon_audio)
                .into(holder.getView(R.id.iv_adapter_relax))
        }
    }

    inner class SearchAdapter(data: MutableList<VideoData>) :
        BaseQuickAdapter<VideoData, BaseViewHolder>(R.layout.layout_adapter_relax_search) {
        override fun convert(holder: BaseViewHolder, item: VideoData) {
            holder.setText(R.id.tv_relax_search_title, item.video_title)
        }

    }
}