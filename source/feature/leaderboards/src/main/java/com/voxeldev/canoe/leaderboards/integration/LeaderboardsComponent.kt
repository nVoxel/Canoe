package com.voxeldev.canoe.leaderboards.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.leaderboards.Leaderboards
import com.voxeldev.canoe.leaderboards.Leaderboards.Output
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStoreProvider
import com.voxeldev.canoe.utils.extensions.asValue

/**
 * @author nvoxel
 */
class LeaderboardsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
) : Leaderboards, ComponentContext by componentContext {

    private val stateMapper: StateMapper = StateMapper()

    private val store = instanceKeeper.getStore {
        LeaderboardsStoreProvider(storeFactory = storeFactory).provide()
    }

    override val model: Value<Leaderboards.Model> = store.asValue().map { state -> stateMapper.toModel(state) }

    override fun onItemClicked(profileUrl: String) = output(Output.Selected(profileUrl = profileUrl))

    override fun onReloadClicked() = store.accept(intent = LeaderboardsStore.Intent.ReloadLeaderboards)

    override fun onToggleFilterBottomSheet() = store.accept(intent = LeaderboardsStore.Intent.ToggleFilterBottomSheet)

    override fun onSelectLanguage(language: String) = store.accept(
        intent = LeaderboardsStore.Intent.SetLanguage(language = language),
    )

    override fun onSelectHireable(hireable: Boolean) = store.accept(
        intent = LeaderboardsStore.Intent.SetHireable(hireable = hireable),
    )

    override fun onSelectCountryCode(countryCode: String) = store.accept(
        intent = LeaderboardsStore.Intent.SetCountryCode(countryCode = countryCode),
    )

    override fun onResetFilters() = store.accept(intent = LeaderboardsStore.Intent.ResetFilters)
}
