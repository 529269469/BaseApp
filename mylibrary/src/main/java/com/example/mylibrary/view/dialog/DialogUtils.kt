package com.example.mylibrary.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.mylibrary.R

import com.example.mylibrary.expand.dp2Px

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-10-12 17:37.
 * Description : dialog工具类
 */
object DialogUtils {
    private val pwdList = mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "清空", "0", "")
    private val lineWidth = 1.dp2Px()
    private var pwdContent: String = ""
    private var create: AlertDialog? = null

    /**
     * 输入密码dialog
     */
    fun Context.showInputPWDDialog(isUpload: () -> Unit) {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_input_pwd, null)
        val mDialog = Dialog(this, R.style.AlertDialogStyle)
        mDialog.setContentView(view)
        val tvPass1 = view.findViewById<TextView>(R.id.tv_pass1)
        val tvPass2 = view.findViewById<TextView>(R.id.tv_pass2)
        val tvPass3 = view.findViewById<TextView>(R.id.tv_pass3)
        val tvPass4 = view.findViewById<TextView>(R.id.tv_pass4)
        val tvPass5 = view.findViewById<TextView>(R.id.tv_pass5)
        val tvPass6 = view.findViewById<TextView>(R.id.tv_pass6)
//        view.findViewById<TextView>(R.id.tvForgetPwd).setOnClickListener { this.startToActivity<ZhifumimaActivity>() }
        val tvPwdTitle = view.findViewById<TextView>(R.id.tvPwdTitle)
        view.findViewById<ImageView>(R.id.ivClosePwd).setOnClickListener { mDialog.dismiss() }
        val rvPwdKeyboard = view.findViewById<RecyclerView>(R.id.rvPwdKeyboard)
        rvPwdKeyboard.layoutManager = GridLayoutManager(this, 3)
        rvPwdKeyboard.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val manager = parent.layoutManager as GridLayoutManager
                val childCount = parent.childCount
                val spanCount = manager.spanCount
                val offset = lineWidth / 2
                for (i in 0 until childCount) {
                    if (i < spanCount) when {
                        i % spanCount == 0 -> outRect.set(0, offset, offset, 0);
                        i % spanCount == spanCount - 1 -> outRect.set(offset, offset, 0, 0)
                        else -> outRect.set(offset, offset, offset, 0)
                    } else when {
                        i % spanCount == 0 -> outRect.set(0, lineWidth, offset, 0)
                        i % spanCount == spanCount - 1 -> outRect.set(offset, lineWidth, 0, 0)
                        else -> outRect.set(offset, lineWidth, offset, 0)
                    }
                }
            }
        })
        val pwdKeyBoardAdapter = PwdKeyBoardAdapter(pwdList)
        rvPwdKeyboard.adapter = pwdKeyBoardAdapter
        pwdKeyBoardAdapter.setOnItemClickListener { _, _, position ->
            pwdContent =
                "${tvPass1.text}${tvPass2.text}${tvPass3.text}${tvPass4.text}${tvPass5.text}${tvPass6.text}"
            when (position) {
                9 -> {
                    tvPass1.text = ""
                    tvPass2.text = ""
                    tvPass3.text = ""
                    tvPass4.text = ""
                    tvPass5.text = ""
                    tvPass6.text = ""
                }
                10 -> when {
                    TextUtils.isEmpty(tvPass1.text) && pwdContent.isEmpty() -> tvPass1.text = "0"
                    TextUtils.isEmpty(tvPass2.text) && pwdContent.length == 1 -> tvPass2.text = "0"
                    TextUtils.isEmpty(tvPass3.text) && pwdContent.length == 2 -> tvPass3.text = "0"
                    TextUtils.isEmpty(tvPass4.text) && pwdContent.length == 3 -> tvPass4.text = "0"
                    TextUtils.isEmpty(tvPass5.text) && pwdContent.length == 4 -> tvPass5.text = "0"
                    TextUtils.isEmpty(tvPass6.text) && pwdContent.length == 5 -> tvPass6.text = "0"
                }
                11 -> when {
                    !TextUtils.isEmpty(tvPass1.text) && pwdContent.length == 1 -> tvPass1.text = ""
                    !TextUtils.isEmpty(tvPass2.text) && pwdContent.length == 2 -> tvPass2.text = ""
                    !TextUtils.isEmpty(tvPass3.text) && pwdContent.length == 3 -> tvPass3.text = ""
                    !TextUtils.isEmpty(tvPass4.text) && pwdContent.length == 4 -> tvPass4.text = ""
                    !TextUtils.isEmpty(tvPass5.text) && pwdContent.length == 5 -> tvPass5.text = ""
                    !TextUtils.isEmpty(tvPass6.text) && pwdContent.length == 6 -> tvPass6.text = ""
                }
                else -> when {
                    TextUtils.isEmpty(tvPass1.text) && pwdContent.isEmpty() ->
                        tvPass1.text = String.format("%s", position + 1)
                    TextUtils.isEmpty(tvPass2.text) && pwdContent.length == 1 ->
                        tvPass2.text = String.format("%s", position + 1)
                    TextUtils.isEmpty(tvPass3.text) && pwdContent.length == 2 ->
                        tvPass3.text = String.format("%s", position + 1)
                    TextUtils.isEmpty(tvPass4.text) && pwdContent.length == 3 ->
                        tvPass4.text = String.format("%s", position + 1)
                    TextUtils.isEmpty(tvPass5.text) && pwdContent.length == 4 ->
                        tvPass5.text = String.format("%s", position + 1)
                    TextUtils.isEmpty(tvPass6.text) && pwdContent.length == 5 ->
                        tvPass6.text = String.format("%s", position + 1)
                }
            }
            if (pwdContent.length == 6) {
                isUpload.invoke()
            }
        }
        mDialog.window?.let {
            it.setGravity(Gravity.BOTTOM)
            val lp = it.attributes
            lp.alpha = 1f
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.attributes = lp
        }
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    /**
     * 被踢下线工具类
     * 初始化与逻辑不分离调用无参方法  分离就调用有参方法
     */
    fun FragmentActivity.kickedOffline() {
        if (create == null)
            create = AlertDialog.Builder(this).create()
        kickedOffline(create)
    }

    fun kickedOffline(create: AlertDialog? = null) {
        create?.let { dialog ->
            if (!dialog.isShowing) {
                dialog.window?.let { window ->
                    window.setWindowAnimations(R.style.kickedOfflineStyle)
                    window.setBackgroundDrawableResource(android.R.color.transparent)
                    window.setGravity(Gravity.CENTER)
                    dialog.show()
                    window.setContentView(R.layout.dialog_kicked_offline)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    window.setLayout(MATCH_PARENT, MATCH_PARENT)
                    window.findViewById<TextView>(R.id.tvKOEnter).setOnClickListener {
                    }
                }
            } else dialog.dismiss()
        }
    }

    private class PwdKeyBoardAdapter(dataList: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_pwd_board_layout, dataList) {
        override fun convert(holder: BaseViewHolder, item: String) {
            if (holder.adapterPosition == 9) holder.setBackgroundColor(
                R.id.tvItemNumber,
                Color.parseColor("#cccccc")
            )
            if (holder.adapterPosition == 11) {
                holder.setGone(R.id.tvItemNumber, true)
                holder.setGone(R.id.ivDeletePwd, false)
            }
            holder.setText(R.id.tvItemNumber, item)
        }
    }
}