package com.wusong.router

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/** 雾凇统一转场时长 */
private const val RIME_TRANSITION_MS = 260

/**
 * 雾凇导航宿主：统一页面转场动画（水平进出 + 淡入淡出），
 * 业务侧只关心 [builder] 里的页面注册。
 *
 * ```kotlin
 * val navController = rememberNavController()
 * RimeNavHost(navController = navController, startDestination = AppRoutes.Home) {
 *     composable(AppRoutes.Home) { HomeRoute() }
 *     composable(AppRoutes.Detail) { DetailRoute() }
 * }
 * ```
 */
@Composable
fun RimeNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    durationMillis: Int = RIME_TRANSITION_MS,
    builder: NavGraphBuilder.() -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(durationMillis)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
            )
        },
        builder = builder,
    )
}

/** 与 [RimeNavHost] 一致的手动转场，供非 NavHost 场景复用 */
fun rimeEnterOffset(durationMillis: Int = RIME_TRANSITION_MS) =
    slideInHorizontally(animationSpec = tween(durationMillis)) { it }

fun rimeExitOffset(durationMillis: Int = RIME_TRANSITION_MS) =
    slideOutHorizontally(animationSpec = tween(durationMillis)) { it }
