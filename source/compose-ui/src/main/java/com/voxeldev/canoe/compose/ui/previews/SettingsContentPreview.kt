package com.voxeldev.canoe.compose.ui.previews

import androidx.compose.runtime.Composable
import com.voxeldev.canoe.compose.ui.content.settings.SettingsContent
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.settings.Settings

/**
 * @author nvoxel
 */
@Composable
internal fun SettingsContentPreview() {
    val model = Settings.Model(
        isConnected = true,
        errorText = null,
        isLoading = false,
    )

    CanoeTheme {
        SettingsContent(
            model = model,
            onReloadButtonClicked = {},
            onConnectButtonClicked = {},
            onDisconnectButtonClicked = {},
        )
    }
}
