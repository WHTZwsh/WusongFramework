package com.wusong.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wusong.arch.paging.PagingController
import com.wusong.arch.paging.PagingStatus
import com.wusong.core.WsError
import com.wusong.core.displayMessage
import com.wusong.core.retryable
import com.wusong.ui.collectAsUiState

/**
 * 分页列表：把 [PagingController] 直接渲染成 LazyColumn，
 * 内含下拉刷新、自动预加载、底部状态条、空态占位。
 *
 * ```kotlin
 * WsPagingLazyColumn(controller = vm.paging) { _, post ->
 *     PostItem(post) { dispatch(PostIntent.OpenDetail(post.id)) }
 * }
 * ```
 *
 * @param preloadThreshold 列表剩余可展示项少于该值时自动加载下一页；
 *                         首屏不足一屏时也会自动续拉，无需手写触底监听
 */
@Composable
fun <T : Any> WsPagingLazyColumn(
    controller: PagingController<T>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Int = 0,
    refreshEnabled: Boolean = true,
    preloadThreshold: Int = 5,
    itemKey: ((T) -> Any)? = null,
    emptyContent: @Composable () -> Unit = { WsEmpty() },
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) {
    val items = controller.items.collectAsUiState()
    val status = controller.status.collectAsUiState()
    var error by remember { mutableStateOf<WsError?>(null) }
    LaunchedEffect(controller) {
        controller.errors.collect { error = it }
    }
    val scope = rememberCoroutineScope()

    // 统一的加载驱动：数据不够展示 / 处于空闲态就续拉下一页
    LaunchedEffect(items.size, status) {
        if (status == PagingStatus.Idle) controller.loadMore(scope)
    }

    WsRefreshBox(
        isRefreshing = status == PagingStatus.Refreshing,
        onRefresh = { controller.refresh(scope) },
        enabled = refreshEnabled,
        modifier = modifier,
    ) {
        if (items.isEmpty() && status == PagingStatus.End) {
            Box(Modifier.fillMaxSize()) { emptyContent() }
            return@WsRefreshBox
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(itemSpacing.dp),
        ) {
            pagedItems(items = items, itemKey = itemKey, itemContent = itemContent)

            if (items.isNotEmpty()) {
                item(key = "ws_paging_footer") {
                    WsPagingFooter(
                        status = status,
                        error = if (status == PagingStatus.Error) error else null,
                        onRetry = { controller.retry(scope) },
                    )
                }
            }
        }
    }
}

/** 列表项写入，抽出来便于统一处理 key */
private fun <T : Any> LazyListScope.pagedItems(
    items: List<T>,
    itemKey: ((T) -> Any)?,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit,
) {
    if (itemKey == null) {
        itemsIndexed(items) { index, item -> itemContent(index, item) }
    } else {
        itemsIndexed(items, key = { _, item -> itemKey(item) }) { index, item ->
            itemContent(index, item)
        }
    }
}

/** 列表底部状态条：加载中 / 没有更多 / 失败重试 */
@Composable
fun WsPagingFooter(
    status: PagingStatus,
    error: WsError?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clickableModifier = if (status == PagingStatus.Error) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onRetry,
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .then(clickableModifier),
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            PagingStatus.LoadingMore -> WsFooterText("加载中…")
            PagingStatus.End -> WsFooterText("没有更多了")
            PagingStatus.Error -> {
                val text = error?.displayMessage ?: "加载失败"
                WsFooterText(
                    text = if (error?.retryable != false) "$text · 点击重试" else text,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            PagingStatus.Idle,
            PagingStatus.Refreshing,
            -> Unit
        }
    }
}

@Composable
private fun WsFooterText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Text(text = text, style = MaterialTheme.typography.bodySmall, color = color)
}
