package com.example.baseapp.base_app.network

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-08-28 11:55.
 * Description :
 */
object URLs {
    @JvmField
    @DefaultDomain
//    var BASE_URL = "http://182.92.91.203:8000/"
    var BASE_URL = "https://xcx.mama-online.cn/"

    //检查是否被其他用户登录接口
    const val isLOGIN = "is_login"

    //获取最新语言包数据
    const val LANG_VER = "lang_ver?ver="

    //商品订单详情
    const val GOODS_ORDER_DETAILS = "goods/goods_order_details"

    //介绍列表
    const val INTRODUCELIST = "introducelist"
    //条款
    const val INTRODUCELIST_CATEGORYS = "introducelist_categorys"

    //帮助GET
    const val HELPLIST = "helplist"

    //订单支付初始信息
    const val ORDER_PAY = "goods/order_pay"


    //提现贡献值初始信息(21-09-11号新接口)
    const val NEW_ZFB_TOACCOUNT_TRANSFER = "userwallet/new_zfb_toaccount_transfer?user_id="
    const val NEW_ZFB_TOACCOUNT_TRANSFERs = "userwallet/new_zfb_toaccount_transfer"
    const val WITHDRAWAL_RECORD = "userwallet/withdrawal_record"
    const val WITHDRAWAL_UNDO = "userwallet/withdrawal_undo"
    const val ORDER_MONEY_DETAILS = "homepage/order_money_details"

    //贡献值查看(21-09-11号新接口)
    const val PAYS_ORDER_INFO = "new_pays_order_info?"

    /**
     * 生成支付参数
     */
    const val GAIN_PAY_PARAM = "gain_pay_param"

    //商品详情接口(21-09-16号新接口)
    const val GOODS_DETAIL = "goods/detail/"

    //商品详情中的其他商品和商家接口(21-09-16号新接口)
    const val GOODS_OTHER_GOODS = "goods/other_goods/"

    //商品举报接口
    const val GOODS_REPORTS = "goods_reports"

    //收藏接口
    const val GOODS_COLLECTION = "goods_collection"

    //取消收藏接口
    const val GOODS_COLLECTION_DELETE = "goods_collection_delete"

    //主页查看
    const val USER_COMPANY_INFO = "user_company_info?"

    //主页更多
    const val MORE = "more"

    //主页个人资料
    const val INFO_AUTH = "company/info_auth?"

    //主页个人资料
    const val SLIDE_SHOW = "ad/slideshow_info?"

    //引流二维码绑定
    const val BINDING_QRCODE = "homepage/binding_qrcode/"

    //加入指定团队
    const val TEAM_JOIN = "homepage/team_join/"


}
