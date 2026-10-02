package com.wusong.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 日间：霜白底 + 冰蓝主色 */
private val LightColors = lightColorScheme(
    primary = RimeColor.Ice500,
    onPrimary = Color.White,
    primaryContainer = RimeColor.Frost100,
    onPrimaryContainer = RimeColor.Ice700,
    secondary = RimeColor.Ice400,
    onSecondary = Color.White,
    secondaryContainer = RimeColor.Frost200,
    onSecondaryContainer = RimeColor.Ice700,
    tertiary = RimeColor.Aurora,
    onTertiary = Color.White,
    background = RimeColor.Frost50,
    onBackground = RimeColor.Ink,
    surface = Color.White,
    onSurface = RimeColor.Ink,
    surfaceVariant = RimeColor.Frost100,
    onSurfaceVariant = RimeColor.Ink60,
    outline = RimeColor.Frost400,
    error = RimeColor.Danger,
    onError = Color.White,
)

/** 夜间：夜潭底 + 提亮的冰蓝 */
private val DarkColors = darkColorScheme(
    primary = RimeColor.Ice400,
    onPrimary = Color(0xFF05202B),
    primaryContainer = Color(0xFF14384A),
    onPrimaryContainer = RimeColor.Rime300,
    secondary = RimeColor.Rime300,
    onSecondary = Color(0xFF05202B),
    secondaryContainer = Color(0xFF1B3A47),
    onSecondaryContainer = RimeColor.Frost200,
    tertiary = RimeColor.Aurora,
    onTertiary = Color(0xFF05202B),
    background = RimeColor.InkDarkBg,
    onBackground = RimeColor.Frost100,
    surface = RimeColor.InkDarkSurface,
    onSurface = RimeColor.Frost100,
    surfaceVariant = Color(0xFF1B3340),
    onSurfaceVariant = RimeColor.Frost400,
    outline = Color(0xFF3A5A68),
    error = Color(0xFFE8877C),
    onError = Color(0xFF3A0B08),
)

/** 雾凇圆角：偏大的圆角营造冰晶的圆润感 */
private val RimeShapes = Shapes(
    extraSmall = 6.dp,
    small = 10.dp,
    medium = 14.dp,
    large = 18.dp,
    extraLarge = 26.dp,
)

/** 在 Material 默认排版基础上，略微收紧标题字重 */
private val RimeTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
)

/**
 * 雾凇框架主题入口。
 *
 * ```kotlin
 * RimeTheme {
 *     AppNavHost()
 * }
 * ```
 */
@Composable
fun RimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    /** Android 12+ 取壁纸色；框架内默认关闭以保证品牌一致性 */
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = RimeShapes,
        typography = RimeTypography,
        content = content,
    )
}
