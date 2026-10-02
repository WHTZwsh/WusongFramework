package com.wusong.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PullToRefreshBox
import androidx.compose.material3.PullToRefreshDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 下拉刷新容器：基于 Material3 PullToRefreshBox，配色跟随 [MaterialTheme]。
 *
 * @param enabled 关闭后即退化为普通容器（详情页等场景）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WsRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (!enabled) {
        content()
        return
    }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = it,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter),
                containerColor = MaterialTheme.colorScheme.surface,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        content = { content() },
    )
}
