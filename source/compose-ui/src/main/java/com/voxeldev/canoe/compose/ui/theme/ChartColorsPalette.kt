package com.voxeldev.canoe.compose.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * @author nvoxel
 */
@Immutable
internal data class ChartColorsPalette(
    val red: Color = Color.Unspecified,
    val green: Color = Color.Unspecified,
    val yellow: Color = Color.Unspecified,
    val blue: Color = Color.Unspecified,
    val cyan: Color = Color.Unspecified,
    val orange: Color = Color.Unspecified,
    val magenta: Color = Color.Unspecified,
    val purple: Color = Color.Unspecified,
)

internal val LocalChartColorsPalette = staticCompositionLocalOf { ChartColorsPalette() }

internal val LightChartColorsPalette = ChartColorsPalette(
    red = Color(color = 0xFFC00100),
    green = Color(color = 0xFF026E00),
    yellow = Color(color = 0xFF735C00),
    blue = Color(color = 0xFF343DFF),
    cyan = Color(color = 0xFF006A6A),
    orange = Color(color = 0xFF994700),
    magenta = Color(color = 0xFFA900A9),
    purple = Color(color = 0xFF8500F0),
)

internal val DarkChartColorsPalette = ChartColorsPalette(
    red = Color(color = 0xFFFFB4A8),
    green = Color(color = 0xFF02E600),
    yellow = Color(color = 0xFFEFC107),
    blue = Color(color = 0xFFBEC2FF),
    cyan = Color(color = 0xFF00DDDD),
    orange = Color(color = 0xFFFFB68B),
    magenta = Color(color = 0xFFFFABF3),
    purple = Color(color = 0xFFDAB9FF),
)
