package com.voxeldev.canoe.compose.ui.settings

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
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.Error
import com.voxeldev.canoe.compose.ui.components.Loader
import com.voxeldev.canoe.settings.integration.SettingsComponent

/**
 * @author nvoxel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(component: SettingsComponent) {
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (model.isLoading) Loader()

                model.errorText?.let {
                    Error(
                        message = it,
                        shouldShowRetry = true,
                        retryCallback = component::onReloadButtonClicked,
                    )
                } ?: run {
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "Account Status: ${if (model.isConnected) "Connected" else "Not connected"}",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    if (model.isConnected) {
                        OutlinedButton(onClick = { component.onDisconnectButtonClicked() }) {
                            Text(text = "Disconnect")
                        }
                    } else {
                        Button(onClick = { component.onConnectButtonClicked() }) {
                            Text(text = "Connect")
                        }
                    }
                }
            }
        },
    )
}
