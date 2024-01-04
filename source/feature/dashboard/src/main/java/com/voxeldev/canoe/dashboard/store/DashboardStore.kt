package com.voxeldev.canoe.dashboard.store

import com.arkivanov.mvikotlin.core.store.Store
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.store.DashboardStore.Intent
import com.voxeldev.canoe.dashboard.store.DashboardStore.State
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * @author nvoxel
 */
internal interface DashboardStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data object ShowDatePickerBottomSheet : Intent()
        data class DismissDatePickerBottomSheet(val startMillis: Long?, val endMillis: Long?) : Intent()
        data object ReloadDashboard : Intent()
    }

    data class State(
        val projectName: String? = null,
        val summariesModel: SummariesModel? = null,
        val programLanguagesModel: ProgramLanguagesModel? = null,
        val allTimeModel: AllTimeModel? = null,
        val errorText: String? = null,
        val isSummariesLoading: Boolean = true,
        val isProgramLanguagesLoading: Boolean = true,
        val isAllTimeLoading: Boolean = true,
        val datePickerBottomSheetState: DatePickerBottomSheetState = DatePickerBottomSheetState(),
    )

    data class DatePickerBottomSheetState(
        val active: Boolean,
        val startDate: String,
        val endDate: String,
    ) {

        constructor(
            active: Boolean = false,
            calendar: Calendar = Calendar.getInstance(),
            simpleDateFormat: SimpleDateFormat = SimpleDateFormat(ISO_8601_DATE_FORMAT, Locale.getDefault()),
        ) : this(
            active = active,
            startDate = getCurrentDate(
                calendar = calendar,
                simpleDateFormat = simpleDateFormat,
                daysOffset = DEFAULT_DAYS_OFFSET,
            ),
            endDate = getCurrentDate(calendar = calendar, simpleDateFormat = simpleDateFormat),
        )

        private companion object {
            fun getCurrentDate(calendar: Calendar, simpleDateFormat: SimpleDateFormat, daysOffset: Int = 0): String {
                calendar.add(Calendar.DAY_OF_MONTH, daysOffset)
                return simpleDateFormat.format(calendar.time).also {
                    calendar.add(Calendar.DAY_OF_MONTH, -daysOffset)
                }
            }
        }
    }

    private companion object {
        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd"
        const val DEFAULT_DAYS_OFFSET = -6
    }
}
