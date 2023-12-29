package com.voxeldev.canoe.settings

import com.arkivanov.decompose.value.Value

/**
 * @author nvoxel
 */
interface Settings {

    val model: Value<Model>

    fun onConnectButtonClicked()

    fun onDisconnectButtonClicked()

    fun onReloadButtonClicked()

    data class Model(
        val isConnected: Boolean,
        val errorText: String?,
        val isLoading: Boolean,
    )

    sealed class Output {
        data class Connect(val connectUrl: String) : Output()
    }
}
