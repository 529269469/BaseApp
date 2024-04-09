package com.example.psychology.ui.fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.text.Html
import android.text.Spanned
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.psychology.BR
import com.example.psychology.R
import com.example.psychology.base_app.base.BaseMVVMFragment
import com.example.psychology.base_app.base.BaseViewModel
import com.example.psychology.base_app.expand.loge
import com.example.psychology.base_app.expand.showToast
import com.example.psychology.base_app.utils.CreationChart
import com.example.psychology.base_app.utils.CreationChartBrain
import com.example.psychology.base_app.utils.SpacesItemDecoration
import com.example.psychology.databinding.FragmentReportDetailsBinding
import com.example.psychology.romm.base.AppDatabase
import com.example.psychology.ui.Connotation
import com.example.psychology.ui.HomeJavcActivity
import com.example.psychology.ui.LoginActivity
import com.example.psychology.ui.view.IsPlayPopupWindow
import com.example.psychology.ui.view.LoadingDialog
import com.lwb.radarchart.RadarChartView
import com.tencent.mmkv.MMKV
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * 我的报告详情
 */
class ReportDetailsFragment :
    BaseMVVMFragment<FragmentReportDetailsBinding, BaseViewModel>(R.layout.fragment_report_details) {

    private var myAdapter1: MyAdapter? = null
    private var myAdapter2: MyAdapter? = null

    private var pdfAdapter: PDFAdapter? = null
    private val listpdf = mutableListOf<String>()

    override fun initObserver() {

    }

    var id: Long? = null

    var chartdelta: CreationChartBrain? = null
    var charttheta: CreationChartBrain? = null
    var chartlowAlpha: CreationChartBrain? = null
    var charthighAlpha: CreationChartBrain? = null
    var chartlowBeta: CreationChartBrain? = null
    var charthighBeta: CreationChartBrain? = null
    var chartlowGamma: CreationChartBrain? = null
    var chartmidGamma: CreationChartBrain? = null

    private var setChart: CreationChart? = null

    override fun initData() {
        id = arguments?.getLong("id")

        val db = Room.databaseBuilder(
            requireActivity(),
            AppDatabase::class.java, "users_dp"
        ).build()
        Thread {
            val reportDataDao = db.ReportDataDao()
            val radarChartDao = db.radarChartDao()
            val radarChart1 = radarChartDao.findByMonth(id!!, 1)
            val radarChart2 = radarChartDao.findByMonth(id!!, 2)


            val heartRateList = radarChartDao.findByMonth(id!!, 6)
            val hrvList = radarChartDao.findByMonth(id!!, 7)
            val bloodOxygenList = radarChartDao.findByMonth(id!!, 8)
            var heartRate = 0
            if (heartRateList.isNotEmpty()) {
                heartRateList.forEach { heartRateList ->
                    if (heartRateList.num!! < 30) {
                        heartRateList.num = 80
                    }
                    if (heartRateList.num!! > 140) {
                        heartRateList.num = 80
                    }
                    heartRate += heartRateList.num!!
                }
                heartRate /= heartRateList.size
            }

            var hrv = 0
            if (hrvList.isNotEmpty()) {
                hrvList.forEach { hrvList ->
                    hrv += hrvList.num!!
                }
                hrv /= hrvList.size
            }

            var bloodOxygen = 0
            if (bloodOxygenList.isNotEmpty()) {
                bloodOxygenList.forEach { bloodOxygenList ->
                    bloodOxygen += bloodOxygenList.num!!
                }
                bloodOxygen /= bloodOxygenList.size
            }

            val brainDao = db.brainDao()
            val brainList = brainDao.findByMonth(id!!)

            val reportData = reportDataDao.loadAllById(id!!)

            val myList1 = mutableListOf<ReportDetailsData>()
            val myList2 = mutableListOf<ReportDetailsData>()

            myList1.add(ReportDetailsData("专注度", reportData.zhuanzhu_front))
            myList1.add(ReportDetailsData("放松度", reportData.fangsong_front))
            myList1.add(ReportDetailsData("压力值", reportData.yali_front))
            myList1.add(ReportDetailsData("情绪值", reportData.qingxu_front))
            myList1.add(ReportDetailsData("疲劳值", reportData.pilao_front))

            myList2.add(ReportDetailsData("专注度", reportData.zhuanzhu_later))
            myList2.add(ReportDetailsData("放松度", reportData.fangsong_later))
            myList2.add(ReportDetailsData("压力值", reportData.yali_later))
            myList2.add(ReportDetailsData("情绪值", reportData.qingxu_later))
            myList2.add(ReportDetailsData("疲劳值", reportData.pilao_later))

            activity?.runOnUiThread {
                for (i in radarChart1.indices) {
                    setChart?.AddData(
                        radarChart1[i].num!!.toFloat(),
                        radarChart2[i].num!!.toFloat(),
                    )
                }

                bind.tvReportDetailsHeartRate.text = "心率:$heartRate"
                bind.tvReportDetailsHrv.text = "hrv:$hrv"
                bind.tvReportDetailsBloodOxygen.text = "血氧:$bloodOxygen"

                brainList.forEach { brainList ->
                    chartdelta?.AddData(brainList.delta!!.toFloat())
                    charttheta?.AddData(brainList.theta!!.toFloat())
                    chartlowAlpha?.AddData(brainList.lowAlpha!!.toFloat())
                    charthighAlpha?.AddData(brainList.highAlpha!!.toFloat())
                    chartlowBeta?.AddData(brainList.lowBeta!!.toFloat())
                    charthighBeta?.AddData(brainList.highBeta!!.toFloat())
                    chartlowGamma?.AddData(brainList.lowGamma!!.toFloat())
                    chartmidGamma?.AddData(brainList.midGamma!!.toFloat())

                }
//                setChart?.setMax(radarChart1.size)
                var charSequence: Spanned? = null
                var charSequence2: Spanned? = null
                var report_dirll_score = 0
                var report_mind_score = 0
                if (reportData.yali_front == 0 && reportData.qingxu_front == 0 && reportData.pilao_front == 0) {
                    charSequence = Html.fromHtml(
                        "${Connotation().before_measurement_concentration(reportData.zhuanzhu_front!!)} " +
                                "<br>${Connotation().before_measurement_relax(reportData.fangsong_front!!)}"
                    )

                    charSequence2 = Html.fromHtml(
                        "${
                            Connotation().aftertest_concentration(
                                reportData.zhuanzhu_later!!,
                                reportData.zhuanzhu_effectiveness!!
                            )
                        } " +
                                "<br>${
                                    Connotation().aftertest_relax(
                                        reportData.fangsong_later!!,
                                        reportData.fangsong_effectiveness!!
                                    )
                                }"
                    )

                    report_dirll_score =
                        (reportData.zhuanzhu_centre!! * 0.4
                                + reportData.fangsong_centre!! * 0.5).toInt()
                    report_mind_score =
                        (reportData.zhuanzhu_effectiveness!! * 0.4
                                + reportData.fangsong_effectiveness!! * 0.5).toInt()
                } else {
                    charSequence = Html.fromHtml(
                        "${Connotation().before_measurement_concentration(reportData.zhuanzhu_front!!)} " +
                                "<br>${Connotation().before_measurement_relax(reportData.fangsong_front!!)}" +
                                "<br>${Connotation().before_measurement_pressure(reportData.yali_front!!)}" +
                                "<br>${Connotation().before_measurement_emotion(reportData.qingxu_front!!)}" +
                                "<br>${Connotation().before_measurement_tired(reportData.pilao_front!!)}"
                    )

                    charSequence2 = Html.fromHtml(
                        "${
                            Connotation().aftertest_concentration(
                                reportData.zhuanzhu_later!!,
                                reportData.zhuanzhu_effectiveness!!
                            )
                        } " + "<br>${
                            Connotation().aftertest_relax(
                                reportData.fangsong_later!!,
                                reportData.fangsong_effectiveness!!
                            )
                        }" + "<br>${
                            Connotation().aftertest_pressure(
                                reportData.yali_later!!,
                                reportData.yali_effectiveness!!
                            )
                        }" + "<br>${
                            Connotation().aftertest_emotion(
                                reportData.qingxu_later!!,
                                reportData.qingxu_effectiveness!!
                            )
                        }" + "<br>${
                            Connotation().aftertest_tired(
                                reportData.pilao_later!!,
                                reportData.pilao_effectiveness!!
                            )
                        }"
                    )

                    report_dirll_score =
                        (reportData.zhuanzhu_centre!! * 0.3
                                + reportData.fangsong_centre!! * 0.4
                                + reportData.qingxu_centre!! * 0.1
                                + reportData.yali_centre!! * 0.1
                                + reportData.pilao_centre!! * 0.1).toInt()
                    report_mind_score =
                        (reportData.zhuanzhu_effectiveness!! * 0.3
                                + reportData.fangsong_effectiveness!! * 0.4
                                + reportData.qingxu_effectiveness!! * 0.1
                                + reportData.yali_effectiveness!! * 0.1
                                + reportData.pilao_effectiveness!! * 0.1).toInt()
                }

                bind.tvReportDetailsFront.text = charSequence
                bind.tvReportDetailsAftertest.text = charSequence2

                bind.progressView1.current = report_dirll_score
                bind.tvProgress1.text = "$report_dirll_score"

                bind.progressView2.current = report_mind_score
                bind.tvProgress2.text = "$report_mind_score"
                val charSequenceMind =
                    Html.fromHtml(Connotation().physical_and_mental_state(report_dirll_score))
                val charSequenceDirll =
                    Html.fromHtml(Connotation().training_effectiveness(report_mind_score))

                bind.tvTvReportDetails1.text = charSequenceMind
                bind.tvTvReportDetails2.text = charSequenceDirll

                myAdapter1?.setList(myList1)
                myAdapter2?.setList(myList2)
                bind.lcReportDetails2.insertType("专注度", reportData.zhuanzhu_front!!, 100)
                bind.lcReportDetails2.insertType("放松度", reportData.fangsong_front!!, 100)
                bind.lcReportDetails2.insertType("压力值", reportData.yali_front!!, 100)
                bind.lcReportDetails2.insertType("情绪值", reportData.qingxu_front!!, 100)
                bind.lcReportDetails2.insertType("疲劳值", reportData.pilao_front!!, 100)

                bind.lcReportDetails3.compareType(
                    "专注度",
                    reportData.zhuanzhu_front!!,
                    reportData.zhuanzhu_later!!,
                    100
                )
                bind.lcReportDetails3.compareType(
                    "放松度",
                    reportData.fangsong_front!!,
                    reportData.fangsong_later!!,
                    100
                )
                bind.lcReportDetails3.compareType(
                    "压力值",
                    reportData.yali_front!!,
                    reportData.yali_later!!,
                    100
                )
                bind.lcReportDetails3.compareType(
                    "情绪值",
                    reportData.qingxu_front!!,
                    reportData.qingxu_later!!,
                    100
                )
                bind.lcReportDetails3.compareType(
                    "疲劳值",
                    reportData.pilao_front!!,
                    reportData.pilao_later!!,
                    100
                )
                val config: RadarChartView.Config = RadarChartView.Config()
                    .setCenterPointRadius(5)
                    .setChartWidget(0.8f)
                    .setFillColor(activity?.resources!!.getColor(R.color.color_A6E2EB))
                    .setSecondFillColor(activity?.resources!!.getColor(R.color.color_90F48E8E))
                    .setValueLineSize(1)
                    .setValuePointRadius(5)
                    .setBackgroundColor(activity?.resources!!.getColor(R.color.color_E4EAF0))
                    .setTextColor(activity?.resources!!.getColor(R.color.black))
                    .setTextSize(20)
                    .setCanScroll(true)
                    .setCanFling(true)
                    .setValueBackgroundAlpha(0.2f)
                    .setTextPosition(1.15f)

                bind.lcReportDetails2.setConfig(config)

                bind.lcReportDetails3.setConfig(config)

            }

        }.start()

        val kv = MMKV.defaultMMKV()
        val photo = kv?.getString("photo", "16566668888")
        bind.tvReportDetailsPhoto.text = "咨询热线：$photo"
    }

    override fun initView() {
        bind.llReportDetailsLeft.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        chartdelta = CreationChartBrain(bind.lcReportDelta, "δ波")
        chartdelta?.init()
        charttheta = CreationChartBrain(bind.lcReportTheta, "θ波")
        charttheta?.init()
        chartlowAlpha = CreationChartBrain(bind.lcReportLowAlpha, "low α波")
        chartlowAlpha?.init()
        charthighAlpha = CreationChartBrain(bind.lcReportHighAlpha, "high α波")
        charthighAlpha?.init()
        chartlowBeta = CreationChartBrain(bind.lcReportLowBeta, "low β波")
        chartlowBeta?.init()
        charthighBeta = CreationChartBrain(bind.lcReportHighBeta, "high β波")
        charthighBeta?.init()
        chartlowGamma = CreationChartBrain(bind.lcReportLowGamma, "low γ波")
        chartlowGamma?.init()
        chartmidGamma = CreationChartBrain(bind.lcReportMidGamma, "mid γ波")
        chartmidGamma?.init()

        setChart = CreationChart(bind.lcReportDetails1)
        setChart?.init()

        bind.rvReportDownload.layoutManager = GridLayoutManager(activity, 1)
        pdfAdapter = PDFAdapter(mutableListOf())
        bind.rvReportDownload.adapter = pdfAdapter

        listpdf.add("关于${LoginActivity.name}的报告下载")

        pdfAdapter?.setList(listpdf)
        pdfAdapter?.setOnItemClickListener { _, v, position ->
            val loadingDialog = LoadingDialog(requireActivity(), bind.rvReportDownload)//显示
            Thread {
                //file是你创建的pdf路径
                val currentTimeMillis = System.currentTimeMillis()
                val path = "/storage/emulated/0/Download/pdf/${currentTimeMillis}.pdf"
                path.loge()
                val file = File(path)
                if (!file.exists()) {
                    try {
                        // 获取父文件
                        val parent = file.parentFile
                        if (!parent.exists()) {
                            parent.mkdirs() //创建所有父文件夹
                        }
                        val createNewFile = file.createNewFile()
                        "文件创建成功！！！$createNewFile".loge()
                        val document = PdfDocument()
                        val pageInfo =
                            PageInfo.Builder(bind.rlDetailsPdf.width, bind.rlDetailsPdf.height, 1)
                                .create()
                        val page = document.startPage(pageInfo)
                        bind.rlDetailsPdf.draw(page.canvas)
                        document.finishPage(page)
                        document.writeTo(FileOutputStream(file))
                        document.close()
                        activity?.runOnUiThread {
                            "PDF已保存在：$path".showToast()
                            loadingDialog.dismiss()

                            val isPlayPopupWindow = IsPlayPopupWindow(
                                activity,
                                bind.rvReportDownload,
                                "PDF已保存在\nDownload/pdf目录下\n点击确定将打开pdf"
                            )
                            isPlayPopupWindow.setOnClickListener {
                                val file = File(path)
                                val uri = FileProvider.getUriForFile(
                                    requireActivity(),
                                    "com.example.psychology.fileprovider",
                                    file
                                )
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(uri, "application/pdf")
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 授予读取权限
                                startActivity(intent);
                            }
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }.start()

        }

        bind.rvReportDetailsFront.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bind.rvReportDetailsFront.addItemDecoration(SpacesItemDecoration(22))
        myAdapter1 = MyAdapter(mutableListOf())
        bind.rvReportDetailsFront.adapter = myAdapter1

        bind.rvReportDetailsQueen.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bind.rvReportDetailsQueen.addItemDecoration(SpacesItemDecoration(22))
        myAdapter2 = MyAdapter(mutableListOf())
        bind.rvReportDetailsQueen.adapter = myAdapter2


        bind.llReportDetails.setOnClickListener {

            front_later(
                "报告分数：",
                "本报告中的所有的分数均符合下列情形：\n" +
                        "当X≥85时，你的成绩超过了97.73％的用户；\n" +
                        "当X≥75时，你的成绩超过了84.12％的用户；\n" +
                        "当X≥65时，你的成绩超过了50％的用户；\n" +
                        "当X≥55时，你的成绩超过了15.86％的用户；\n" +
                        "当X≥45时，你的成绩超过了2.27％的用户。"
            )
        }

        bind.llReportDetailsFront.setOnClickListener {
            front_later(
                "前测分析：",
                "前测是在一个实验或干预开始之前对参与者进行的测试。这种测试通常用于获取关于参与者在实验或干预开始之前的基线数据。一般来说，前测的结果将作为比较基准，使研究人员能够清楚地看到实验或干预对参与者有何影响。"
            )
        }

        bind.llReportDetailsLater.setOnClickListener {
            front_later(
                "后测分析：",
                "后测是在实验或干预结束后对参与者进行的测试。它的目的是评估实验或干预是否有效，以及有效的程度如何。后测通常包含与前测相同的问题或任务，以便研究人员可以直接比较前后测分数，从而评估实验或干预的效果。"
            )
        }
    }

    fun front_later(title: String, content: String) {
        val popupWindow = PopupWindow(activity)
        val inflate = layoutInflater.inflate(R.layout.layout_popup_front_later, null)
        popupWindow.contentView = inflate
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable(ColorDrawable(0))
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        val lp: WindowManager.LayoutParams = activity?.window!!.attributes
        lp.alpha = 0.7f
        activity?.window?.attributes = lp

        popupWindow.setOnDismissListener {
            val lp1: WindowManager.LayoutParams = activity?.window!!.attributes
            lp1.alpha = 1f
            activity?.window?.attributes = lp1
        }
        popupWindow.showAtLocation(bind.llReportDetailsFront, Gravity.CENTER, 0, 0)

        inflate.findViewById<TextView>(R.id.tv_layout_popup_front_later_title).text = title
        inflate.findViewById<TextView>(R.id.tv_layout_popup_front_later_content).text = content
    }


    override fun initVariableId() = BR.report_details


    inner class MyAdapter(data: MutableList<ReportDetailsData>) :
        BaseQuickAdapter<ReportDetailsData, BaseViewHolder>(
            R.layout.layout_adapter_report_details_list,
            data
        ) {
        override fun convert(holder: BaseViewHolder, item: ReportDetailsData) {
            holder.setText(R.id.tv_report_details_title, item.title)
                .setText(R.id.tv_report_details_num, "${item.num}")
            holder.getView<View>(R.id.v_report_details_num).layoutParams.width =
                ConvertUtils.dp2px((item.num!! * 2).toFloat())

        }

    }

    data class ReportDetailsData(
        val title: String? = null,
        val num: Int? = null,
    )


    inner class PDFAdapter(data: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.layout_adapter_pdf) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_pdf_name, item)
        }

    }


}