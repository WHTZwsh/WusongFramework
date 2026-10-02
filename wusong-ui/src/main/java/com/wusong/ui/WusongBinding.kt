package com.wusong.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wusong.arch.contract.UiEffect
import com.wusong.arch.contract.UiIntent
import com.wusong.arch.contract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * 状态订阅：生命周期感知，App 进后台自动停止收集。
 *
 * ```kotlin
 * val state = viewModel.uiState.collectAsUiState()
 * ```
 */
@Composable
fun <S : UiState> StateFlow<S>.collectAsUiState(): S = collectAsStateWithLifecycle().value

/**
 * 副作用订阅：只消费一次，不参与重组。
 *
 * ```kotlin
 * viewModel.uiEffect.collectEffect { effect ->
 *     when (effect) { ... }
 * }
 * ```
 */
@Composable
fun <E : UiEffect> Flow<E>.collectEffect(onEffect: suspend (E) -> Unit) {
    LaunchedEffect(this) {
        collect { onEffect(it) }
    }
}

/**
 * 页面契约：一个屏幕 = 状态渲染 + 意图分发。
 * 用接口形式固化写法，便于团队协作与代码评审。
 */
interface WusongScreen<S : UiState, I : UiIntent, E : UiEffect> {

    @Composable
    fun Content(state: S, dispatch: (I) -> Unit)
}
