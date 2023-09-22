package com.example.psychology.base_app.base

interface IBaseView {
    /** 注册监听 */
    fun initObserver()

    /** 初始化数据 */
    fun initData()

    /** 初始化View */
    fun initView()

    /** variable绑定 */
    fun initVariableId(): Int?
}