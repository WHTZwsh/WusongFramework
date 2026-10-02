package com.wusong.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** 按钮语义：决定配色与视觉权重 */
enum class WsButtonType {
    /** 主行动，一屏最多一个 */
    Primary,

    /** 次行动 */
    Secondary,

    /** 弱行动，如「取消」「跳过」 */
    Text,
}

/**
 * 雾凇按钮：支持加载态（加载中自动禁用点击并显示转圈）。
 *
 * ```kotlin
 * WsButton("提交", loading = state.submitting) { dispatch(Submit) }
 * ```
 */
@Composable
fun WsButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: WsButtonType = WsButtonType.Primary,
    icon: ImageVector? = null,
) {
    val clickable = enabled && !loading
    val content: @Composable () -> Unit = {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = when (type) {
                    WsButtonType.Primary -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.primary
                },
            )
            Spacer(Modifier.width(8.dp))
        } else if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }

    when (type) {
        WsButtonType.Primary -> Button(
            onClick = onClick,
            modifier = modifier.height(48.dp),
            enabled = clickable,
            content = { content() },
        )

        WsButtonType.Secondary -> OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(48.dp),
            enabled = clickable,
            content = { content() },
        )

        WsButtonType.Text -> TextButton(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            enabled = clickable,
            content = { content() },
        )
    }
}

/** 通栏主按钮，常用于表单底部 */
@Composable
fun WsBlockButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    WsButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        loading = loading,
        type = WsButtonType.Primary,
    )
}

/** 保留给自定义色按钮的默认圆角，与 theme/Shapes 保持一致 */
val WsButtonHeight = 48.dp

private val WsButtonColors = ButtonDefaults.buttonColors()
