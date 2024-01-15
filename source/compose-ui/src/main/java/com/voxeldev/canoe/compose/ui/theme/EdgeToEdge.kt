package com.voxeldev.canoe.compose.ui.theme

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

private val statusBarStyle: SystemBarStyle = SystemBarStyle(Color.TRANSPARENT, Color.TRANSPARENT)

fun ComponentActivity.enableEdgeToEdge(
    navigationBarStyle: SystemBarStyle,
) {
    val view = window.decorView
    val statusBarIsDark = statusBarStyle.detectDarkMode(view.resources)
    val navigationBarIsDark = navigationBarStyle.detectDarkMode(view.resources)

    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = statusBarStyle.getScrim(statusBarIsDark)
    window.navigationBarColor = navigationBarStyle.getScrim(navigationBarIsDark)
    WindowInsetsControllerCompat(window, view).run {
        isAppearanceLightStatusBars = !statusBarIsDark
        isAppearanceLightNavigationBars = !navigationBarIsDark
    }
}

class SystemBarStyle(
    private val lightScrim: Int,
    private val darkScrim: Int,
    internal val detectDarkMode: (Resources) -> Boolean = { resources ->
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
    },
) {
    internal fun getScrim(isDark: Boolean) = if (isDark) darkScrim else lightScrim
}
