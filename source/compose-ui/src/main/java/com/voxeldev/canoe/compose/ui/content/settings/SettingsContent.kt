package com.voxeldev.canoe.compose.ui.content.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.Error
import com.voxeldev.canoe.compose.ui.components.Loader
import com.voxeldev.canoe.compose.ui.previews.SettingsContentPreview
import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.integration.SettingsComponent

/**
 * @author nvoxel
 */
@Composable
internal fun SettingsContent(component: SettingsComponent) {
    with(component) {
        val model by model.subscribeAsState()

        SettingsContent(
            model = model,
            onReloadButtonClicked = ::onReloadButtonClicked,
            onConnectButtonClicked = ::onConnectButtonClicked,
            onDisconnectButtonClicked = ::onDisconnectButtonClicked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    model: Settings.Model,
    onReloadButtonClicked: () -> Unit,
    onConnectButtonClicked: () -> Unit,
    onDisconnectButtonClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (model.isLoading) Loader()

                model.errorText?.let {
                    Error(
                        message = it,
                        shouldShowRetry = true,
                        retryCallback = onReloadButtonClicked,
                    )
                } ?: run {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "Account Status: ${if (model.isConnected) "Connected" else "Not connected"}",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    if (model.isConnected) {
                        OutlinedButton(onClick = onDisconnectButtonClicked) {
                            Text(text = "Disconnect")
                        }
                    } else {
                        Button(onClick = onConnectButtonClicked) {
                            Text(text = "Connect")
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun Preview() = SettingsContentPreview()
