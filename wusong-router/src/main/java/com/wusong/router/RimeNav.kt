package com.wusong.router

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * 全局路由入口：把 NavController 托管给框架，
 * 使非 Compose 环境（通知、深链、定时器）也能跳转。
 */
object RimeNav {

    private var controller: NavController? = null

    /** 在 NavHost 创建处调用一次即可 */
    fun attach(controller: NavController) {
        RimeNav.controller = controller
    }

    fun detach() {
        controller = null
    }

    fun navigate(route: String) {
        controller?.navigateOnce(route)
    }

    fun popBackStack(): Boolean = controller?.popBackStack() ?: false
}

/**
 * 单顶跳转 + 连点防抖：300ms 内重复点击同一路由直接丢弃，
 * 避免「列表快速点两下打开两个详情页」这类经典问题。
 */
fun NavController.navigateOnce(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    if (!RimeClickGuard.pass(route)) return
    navigate(route) {
        launchSingleTop = true
        builder()
    }
}

/** 极简防抖：按 route 记录上次触发时间 */
private object RimeClickGuard {

    private const val INTERVAL_MS = 300L
    private val lastClickAt = mutableMapOf<String, Long>()

    @Synchronized
    fun pass(route: String): Boolean {
        val now = System.currentTimeMillis()
        val last = lastClickAt[route] ?: 0L
        return if (now - last < INTERVAL_MS) {
            false
        } else {
            lastClickAt[route] = now
            true
        }
    }
}
