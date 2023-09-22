package com.example.psychology.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.bean.DeleteEvent
import com.example.psychology.databinding.FragmentMyBinding

import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.ui.HomeJavcActivity
import com.example.psychology.ui.LoginActivity

import com.example.psychology.ui.LoginActivity.Companion.head
import com.example.psychology.ui.LoginActivity.Companion.isAdmin
import com.example.psychology.ui.LoginActivity.Companion.name
import com.example.psychology.ui.LoginActivity.Companion.signature
import com.example.psychology.ui.LoginActivity.Companion.user_name
import com.example.psychology.ui.my.MyActivity
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus

/**
 * 我的
 */
class MyFragment : BaseMVVMFragment<FragmentMyBinding, BaseViewModel>(R.layout.fragment_my) {

    private var myAdapter: MyAdapter? = null
    private val list = mutableListOf<MyBean>()

    override fun initObserver() {
    }

    override fun initData() {

        bind.ivMyHead.setImageResource(head!!)
        bind.tvMyName.text = name
        bind.tvMyContent.text = signature

    }

    override fun initView() {
        bind.rvMy.layoutManager = GridLayoutManager(activity, 1)
        myAdapter = MyAdapter(mutableListOf())
        bind.rvMy.adapter = myAdapter
        list.clear()
        list.add(MyBean(R.mipmap.component1, "我的记录"))
        list.add(MyBean(R.mipmap.component2, "我的收藏"))
        list.add(MyBean(R.mipmap.component3, "我的报告"))
        if (isAdmin == true){
            bind.btMyPassword.visibility=View.VISIBLE
            list.add(MyBean(R.mipmap.component3, "我的课程"))
            list.add(MyBean(R.mipmap.component3, "数据中心"))
        }else{
            bind.btMyPassword.visibility=View.GONE
        }
        bind.rvMy.addItemDecoration(SpacesItemDecoration(30))
        myAdapter?.setList(list)

        myAdapter?.setOnItemClickListener { _, _, position ->

            startActivity(Intent(activity, MyActivity::class.java).putExtra("type", position))
        }

        applyDebouncingClickListener(
            bind.ivMyCompile,
            bind.ivMyHead,
            bind.btExit,
            bind.btMyPassword
        )

    }

    override fun onDebouncingClick(v: View?) {
        super.onDebouncingClick(v)
        when (v) {
            bind.ivMyHead -> {
                initHead()
            }

            bind.ivMyCompile -> {
                initCompile()
            }

            bind.btExit -> {
                activity?.startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            bind.btMyPassword -> {
                Navigation.findNavController(v)
                    .navigate(R.id.action_fragment_my_to_passwordFragment)
            }
        }

    }

    /**
     * 选择头像
     */
    var headWindow: PopupWindow? = null
    private var headAdapter: HeadAdapter? = null
    private val headlist = mutableListOf<Int>()
    private fun initHead() {
        headWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_head, null)
        headWindow?.contentView = inflate
        headWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        headWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        headWindow?.setBackgroundDrawable(ColorDrawable(0))
        headWindow?.isFocusable = true
        headWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        headWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
        }
        headWindow?.showAtLocation(bind.ivMyCompile, Gravity.CENTER, 0, 0)

        val rv_head_popup = inflate.findViewById<RecyclerView>(R.id.rv_head_popup)

        rv_head_popup.layoutManager = GridLayoutManager(activity, 5)
        headAdapter = HeadAdapter(mutableListOf())
        rv_head_popup.adapter = headAdapter
        headlist.clear()
        headlist.add(R.mipmap.icon_head11)
        headlist.add(R.mipmap.icon_head12)
        headlist.add(R.mipmap.icon_head13)
        headlist.add(R.mipmap.icon_head14)
        headlist.add(R.mipmap.icon_head15)
        headlist.add(R.mipmap.icon_head16)
        headlist.add(R.mipmap.icon_head17)
        headlist.add(R.mipmap.icon_head18)
        headlist.add(R.mipmap.icon_head19)
        headlist.add(R.mipmap.icon_head20)
        rv_head_popup.addItemDecoration(SpacesItemDecoration(13))
        headAdapter?.setList(headlist)

        headAdapter?.setOnItemClickListener { _, _, position ->
            bind.ivMyHead.setImageResource(headlist[position])

            viewModel.setHead(headlist[position])
            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val userDao = db.userDao()
                val findByName = userDao.findByName(user_name!!)
                findByName.head = headlist[position]
                userDao.update(findByName)
            }.start()

            val deleteEvent = DeleteEvent()
            deleteEvent.head = headlist[position]
            EventBus.getDefault().post(deleteEvent)
            headWindow?.dismiss()
        }


    }


    /**
     * 编辑昵称和签名
     */
    var compileWindow: PopupWindow? = null

    @SuppressLint("MissingInflatedId")
    private fun initCompile() {
        compileWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_compile, null)
        compileWindow?.contentView = inflate
        compileWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        compileWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        compileWindow?.setBackgroundDrawable(ColorDrawable(0))
        compileWindow?.isFocusable = true
        compileWindow?.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        compileWindow?.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
        }
        compileWindow?.showAtLocation(bind.ivMyCompile, Gravity.CENTER, 0, 0)

        val ed_my_name = inflate.findViewById<EditText>(R.id.ed_my_name)
        val ed_my_content = inflate.findViewById<EditText>(R.id.ed_my_content)
        val iv_compile_popup_x = inflate.findViewById<ImageView>(R.id.iv_compile_popup_x)
        val tv_my_photo = inflate.findViewById<TextView>(R.id.tv_my_photo)
        val ed_my_photo = inflate.findViewById<EditText>(R.id.ed_my_photo)
        iv_compile_popup_x.setOnClickListener { compileWindow?.dismiss() }

        if (isAdmin == true){
            tv_my_photo.visibility=View.VISIBLE
            ed_my_photo.visibility=View.VISIBLE
        }else{
            tv_my_photo.visibility=View.GONE
            ed_my_photo.visibility=View.GONE
        }
        val kv = MMKV.defaultMMKV()
        val photo = kv?.getString("photo", "false")
        ed_my_photo.hint=photo

        ed_my_name.hint= name
        ed_my_content.hint=signature

        ed_my_photo.addTextChangedListener {
            kv?.putString("photo", it.toString())
        }
        ed_my_name.addTextChangedListener {
            bind.tvMyName.text = it.toString()
            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val userDao = db.userDao()
                val findByName = userDao.findByName(user_name!!)
                findByName.name = it.toString()
                userDao.update(findByName)
            }.start()

            val deleteEvent = DeleteEvent()
            deleteEvent.title = it.toString()
            EventBus.getDefault().post(deleteEvent)

        }
        ed_my_content.addTextChangedListener {
            bind.tvMyContent.text = it.toString()
            val db = Room.databaseBuilder(
                requireActivity(),
                AppDatabase::class.java, "users_dp"
            ).build()
            Thread {
                val userDao = db.userDao()
                val findByName = userDao.findByName(user_name!!)
                findByName.signature = it.toString()
                userDao.update(findByName)
            }.start()
        }


    }

    override fun initVariableId() = BR.my

    override fun onDestroy() {
        super.onDestroy()
        if (compileWindow != null) {
            compileWindow?.dismiss()
        }
        if (headWindow != null) {
            headWindow?.dismiss()
        }
    }

    inner class MyAdapter(data: MutableList<MyBean>) :
        BaseQuickAdapter<MyBean, BaseViewHolder>(R.layout.layout_adapter_my_list, data) {
        override fun convert(holder: BaseViewHolder, item: MyBean) {
            holder.setText(R.id.tv_my_title, item.title)
                .setImageResource(R.id.iv_my_icon, item.icon)

        }

    }

    inner class HeadAdapter(data: MutableList<Int>) :
        BaseQuickAdapter<Int, BaseViewHolder>(R.layout.layout_adapter_my_head, data) {
        override fun convert(holder: BaseViewHolder, item: Int) {
            holder.setImageResource(R.id.iv_head_head, item)
        }

    }

    data class MyBean(
        val icon: Int,
        val title: String
    )

}