package com.voxeldev.canoe.dashboard.integration

import com.voxeldev.canoe.dashboard.Dashboard
import com.voxeldev.canoe.dashboard.store.DashboardStore.State

/**
 * @author nvoxel
 */
internal class StateMapper {

    fun toModel(state: State): Dashboard.Model =
        Dashboard.Model(
            projectName = state.projectName,
            summariesModel = state.summariesModel,
            programLanguagesModel = state.programLanguagesModel,
            errorText = state.errorText,
            isSummariesLoading = state.isSummariesLoading,
            isProgramLanguagesLoading = state.isProgramLanguagesLoading,
            datePickerBottomSheetModel = Dashboard.DatePickerBottomSheetModel(
                active = state.datePickerBottomSheetState.active,
                startDate = state.datePickerBottomSheetState.startDate,
                endDate = state.datePickerBottomSheetState.endDate,
            ),
        )
}
