package com.wusong.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 雾凇色板：取自「霜白 — 冰蓝 — 雾青 — 夜潭」的自然层次。
 *
 * 命名规则：Frost 为背景/容器，Ice/Rime 为主色与强调色，Ink 为文字，Aurora 为点缀。
 */
object RimeColor {

    // —— 霜白：背景与容器 ——
    val Frost50 = Color(0xFFF7FBFD)
    val Frost100 = Color(0xFFE9F4F9)
    val Frost200 = Color(0xFFD3E7EF)
    val Frost400 = Color(0xFFBAD6E2)

    // —— 冰蓝：主色 ——
    val Ice400 = Color(0xFF6FB6D6)
    val Ice500 = Color(0xFF3E93B8)
    val Ice600 = Color(0xFF2C7A9C)
    val Ice700 = Color(0xFF1E5A75)

    // —— 雾青：辅助色 ——
    val Rime300 = Color(0xFFA9D6E5)
    val Glacier = Color(0xFF8FD3E8)

    // —— 墨色：文字 ——
    val Ink = Color(0xFF0F2530)
    val Ink60 = Color(0xFF48707F)
    val InkDarkBg = Color(0xFF0C1C25)
    val InkDarkSurface = Color(0xFF12252F)

    // —— 点缀与语义色 ——
    val Aurora = Color(0xFF57C7A5)
    val Danger = Color(0xFFC0483C)
    val Warning = Color(0xFFD99A2B)
}
