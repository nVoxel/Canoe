package com.voxeldev.canoe.settings.integration

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.Settings.Output
import com.voxeldev.canoe.settings.store.SettingsStore
import com.voxeldev.canoe.settings.store.SettingsStoreProvider
import com.voxeldev.canoe.utils.extensions.asValue
import com.voxeldev.canoe.utils.parsers.AuthenticationCodeParser
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
class SettingsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val stringResourceProvider: StringResourceProvider,
    private val deepLink: Uri?,
    private val output: (Output) -> Unit,
) : Settings, KoinComponent, ComponentContext by componentContext {

    private val authenticationCodeParser: AuthenticationCodeParser by inject()

    private val store = instanceKeeper.getStore {
        SettingsStoreProvider(
            storeFactory = storeFactory,
            deepLink = deepLink,
            authenticationCodeParser = authenticationCodeParser,
        ).provide()
    }

    override val model: Value<Settings.Model> = store.asValue().map { state ->
        Settings.Model(isConnected = state.isConnected, errorText = state.errorText, isLoading = state.isLoading)
    }

    override fun onConnectButtonClicked() = output(
        Output.Connect(connectUrl = stringResourceProvider.getOAuthAuthorizeUrl()),
    )

    override fun onDisconnectButtonClicked() = store.accept(intent = SettingsStore.Intent.DisconnectAccount)

    override fun onReloadButtonClicked() = store.accept(intent = SettingsStore.Intent.ReloadAccount)
}
