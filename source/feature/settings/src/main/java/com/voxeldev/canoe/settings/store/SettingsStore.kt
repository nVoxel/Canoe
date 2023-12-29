package com.voxeldev.canoe.settings.store

import com.arkivanov.mvikotlin.core.store.Store
import com.voxeldev.canoe.settings.store.SettingsStore.Intent
import com.voxeldev.canoe.settings.store.SettingsStore.State

/**
 * @author nvoxel
 */
internal interface SettingsStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data object DisconnectAccount : Intent()
        data object ReloadAccount : Intent()
    }

    data class State(
        val isConnected: Boolean = false,
        val errorText: String? = null,
        val isLoading: Boolean = false,
    )
}
