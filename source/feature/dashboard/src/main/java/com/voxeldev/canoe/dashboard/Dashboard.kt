package com.voxeldev.canoe.dashboard

import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel

/**
 * @author nvoxel
 */
interface Dashboard {

    val model: Value<Model>

    fun onReloadClicked()

    fun onShowDatePickerBottomSheet()

    fun onDismissDatePickerBottomSheet(startMillis: Long?, endMillis: Long?)

    data class Model(
        val summariesModel: SummariesModel?,
        val programLanguagesModel: ProgramLanguagesModel?,
        val errorText: String?,
        val isSummariesLoading: Boolean,
        val isProgramLanguagesLoading: Boolean,
        val datePickerBottomSheetModel: DatePickerBottomSheetModel,
    )

    data class DatePickerBottomSheetModel(
        val active: Boolean,
        val startDate: String,
        val endDate: String,
    )
}
