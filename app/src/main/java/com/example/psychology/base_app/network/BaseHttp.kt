package com.example.psychology.base_app.network

import android.os.Build
import android.util.Log
import com.example.psychology.base_app.utils.base.apputils.AppManager
import com.example.psychology.base_app.utils.loadingWindow.LoadingUtil
import com.rxlife.coroutine.RxLifeScope
import rxhttp.RxHttp
import rxhttp.toClass
import java.io.File

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-08-28 10:13.
 * Description : 网络请求管理类
 */
class BaseHttp constructor(val rxLifeScope: RxLifeScope) {
    companion object {
        var token = ""
    }

    inline fun <reified T : Any> getMethod(
        url: String, paramsMap: MutableMap<String, *>,
        crossinline responseData: ((T) -> Unit),
        isShowDialog: Boolean? = false,
        callErr: CallErr? = null
    ) {
        val context = AppManager.instance.getContext()!!
        val loadingUtil = LoadingUtil()
        rxLifeScope.launch({
            val await = RxHttp.get(url)
                .addHeader("os", "a")
                .addHeader(
                    "version",
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                )
                .addHeader("device", Build.MODEL)
                .addHeader("token", token)
                .addAll(paramsMap).toClass<T>().await()
            responseData.invoke(await)
        }, {
            callErr?.err(it)
            Log.e("TAG", "getMethod: $it")
        }, {
            if (isShowDialog!!)
                loadingUtil.showProgressDialog(AppManager.instance.getContext())
        }, {
            if (isShowDialog!!)
                loadingUtil.dismissProgressDialog()
        })
    }

    inline fun <reified T : Any> postMethod(
        url: String, paramsMap: MutableMap<String, *>,
        crossinline responseData: ((T)->Unit),
        fileMap: MutableMap<String, String>? = null,
        isShowDialog: Boolean? = false,
        callErr: CallErr? = null
    ) {
        val context = AppManager.instance.getContext()!!
        val loadingUtil = LoadingUtil()
        rxLifeScope.launch({
            val param = RxHttp.postForm(url).addHeader("os", "a")
                .addHeader(
                    "version",
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                )
                .addHeader("device", Build.MODEL)
                .addHeader("token", token)
                .addAll(paramsMap).addAllQuery(paramsMap)
            fileMap?.forEach {
                param.addFile(it.key, File(it.value))
            }
            val await = param.toClass<T>().await()
            responseData.invoke(await)
        }, {
            callErr?.err(it)
            Log.e("TAG", "postMethod: $it")
        }, {
            if (isShowDialog!!)
                loadingUtil.showProgressDialog(AppManager.instance.getContext())
        }, {
            if (isShowDialog!!)
                loadingUtil.dismissProgressDialog()
        })
    }

//    fun getDownloadMethod(
//        url: String,
//        paramsMap: HashMap<String, Any>,
//        savePath: String,
//        responseData: ((Any)->Unit)? = null
//    ) {
//        val context = AppManager.instance.getContext()!!
//        val header = mutableMapOf<String, String>(
//            "os" to "a",
//            "version" to
//                    context.packageManager.getPackageInfo(context.packageName, 0).versionName,
//            "token" to token
//        )
//        rxLifeScope.launch {
//            val await = RxHttp.get(url).addAllHeader(header).addAll(paramsMap)
//                .toDownload(savePath, null, null)
//                .await()
//            responseData?.invoke(await)
//        }
//    }

    interface CallErr {
        fun err(e: Throwable)
    }
}