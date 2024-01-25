package com.voxeldev.canoe.dashboard.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.dashboard.Dashboard
import com.voxeldev.canoe.dashboard.store.DashboardStore
import com.voxeldev.canoe.dashboard.store.DashboardStoreProvider
import com.voxeldev.canoe.utils.extensions.asValue

/**
 * @author nvoxel
 */
class DashboardComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    projectName: String? = null,
) : Dashboard, ComponentContext by componentContext {

    private val stateMapper: StateMapper = StateMapper()

    private val store = instanceKeeper.getStore {
        DashboardStoreProvider(projectName = projectName, storeFactory = storeFactory).provide()
    }

    override val model: Value<Dashboard.Model> = store.asValue().map { state -> stateMapper.toModel(state) }

    override fun onReloadClicked() = store.accept(intent = DashboardStore.Intent.ReloadDashboard)

    override fun onDatesResetClicked() = store.accept(intent = DashboardStore.Intent.DatesReset)

    override fun onShowDatePickerBottomSheet() = store.accept(DashboardStore.Intent.ShowDatePickerBottomSheet)

    override fun onDismissDatePickerBottomSheet(startMillis: Long?, endMillis: Long?) =
        store.accept(
            intent = DashboardStore.Intent.DismissDatePickerBottomSheet(
                startMillis = startMillis,
                endMillis = endMillis,
            ),
        )
}
