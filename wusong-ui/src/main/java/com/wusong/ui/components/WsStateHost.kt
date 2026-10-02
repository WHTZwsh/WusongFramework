package com.wusong.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wusong.arch.PageState
import com.wusong.core.WsError
import com.wusong.core.displayMessage

/**
 * 页面状态宿主：把 [PageState] 的四种形态渲染成对应界面，
 * 业务只需提供 content，加载/空/错误由框架统一负责。
 *
 * ```kotlin
 * WsStateHost(state = state.page, onRetry = { dispatch(Refresh) }) { posts ->
 *     PostList(posts)
 * }
 * ```
 */
@Composable
fun <T> WsStateHost(
    state: PageState<T>,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    loading: @Composable () -> Unit = { WsLoading() },
    empty: @Composable () -> Unit = { WsEmpty(onRetry = onRetry) },
    error: @Composable (WsError) -> Unit = { WsErrorPage(it, onRetry) },
    content: @Composable (T) -> Unit,
) {
    Crossfade(targetState = state, label = "WsStateHost") { current ->
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (current) {
                is PageState.Loading -> loading()
                is PageState.Empty -> empty()
                is PageState.Error -> error(current.error)
                is PageState.Content -> Box(Modifier.fillMaxSize()) { content(current.data) }
            }
        }
    }
}

@Composable
fun WsLoading(modifier: Modifier = Modifier, message: String = "加载中…") {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(36.dp), strokeWidth = 3.dp)
        Spacer(Modifier.height(12.dp))
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun WsEmpty(
    modifier: Modifier = Modifier,
    message: String = "这里还没有内容",
    onRetry: (() -> Unit)? = null,
) {
    WsPlaceholder(
        modifier = modifier,
        message = message,
        actionText = if (onRetry != null) "刷新一下" else null,
        onAction = onRetry,
    )
}

@Composable
fun WsErrorPage(
    error: WsError,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    WsPlaceholder(
        modifier = modifier,
        message = error.displayMessage,
        actionText = if (onRetry != null) "重试" else null,
        onAction = onRetry,
    )
}

/** 通用占位视图：空态与错误态共用 */
@Composable
fun WsPlaceholder(
    modifier: Modifier = Modifier,
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (actionText != null && onAction != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onAction) { Text(actionText) }
        }
    }
}
