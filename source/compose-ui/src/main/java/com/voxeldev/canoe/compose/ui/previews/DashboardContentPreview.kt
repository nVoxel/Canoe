package com.voxeldev.canoe.compose.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.voxeldev.canoe.compose.ui.content.dashboard.DashboardContent
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.compose.ui.theme.LightChartColorsPalette
import com.voxeldev.canoe.dashboard.Dashboard
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeData
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.CumulativeTotal
import com.voxeldev.canoe.dashboard.api.sumaries.DailyAverage
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel

private val baseModel = Dashboard.Model(
    projectName = null,
    summariesModel = SummariesModel(
        dailyChartData = DailyChartData(
            projectsSeries = hashMapOf(
                "Project 1" to mutableListOf(
                    100f to "1 mins",
                    200f to "2 mins",
                    300f to "3 mins",
                    400f to "4 mins",
                ),
                "Project 2" to mutableListOf(
                    400f to "4 mins",
                    300f to "3 mins",
                    0f to "0 mins",
                    100f to "1 mins",
                ),
            ),
            totalLabels = mutableListOf(
                5f to "5 mins",
                5f to "5 mins",
                3f to "3 mins",
                5f to "5 mins",
            ),
            horizontalLabels = mutableListOf(
                "Day 1",
                "Day 2",
                "Day 3",
                "Day 4",
            ),
        ),
        languagesChartData = listOf(
            "Kotlin" to (2f to "2 mins"),
            "Java" to (1f to "1 mins"),
        ),
        editorsChartData = listOf(
            "Android Studio" to Triple(first = 3f, second = "3 mins", third = LightChartColorsPalette.green.toArgb()),
            "IntelliJ IDEA" to Triple(first = 1f, second = "1 mins", third = LightChartColorsPalette.yellow.toArgb()),
        ),
        operatingSystemsChartData = listOf(
            "macOS" to Triple(first = 4f, second = "4 mins", third = LightChartColorsPalette.blue.toArgb()),
            "Ubuntu" to Triple(first = 2f, second = "2 mins", third = LightChartColorsPalette.red.toArgb()),
        ),
        machinesChartData = listOf(
            "MacBook" to Triple(first = 1f, second = "4 mins", third = LightChartColorsPalette.orange.toArgb()),
            "PC" to Triple(first = 1f, second = "3 mins", third = LightChartColorsPalette.purple.toArgb()),
        ),
        cumulativeTotal = CumulativeTotal(10000f, "10h", "10"),
        dailyAverage = DailyAverage(seconds = 1000f, "1h"),
    ),
    programLanguagesModel = ProgramLanguagesModel(
        data = listOf(),
        total = 1,
        totalPages = 1,
    ),
    allTimeModel = AllTimeModel(
        data = AllTimeData(
            decimal = "100.0",
            digital = "100",
            text = "100h",
            totalSeconds = 1000f,
        ),
        message = null,
    ),
    errorText = null,
    isSummariesLoading = false,
    isProgramLanguagesLoading = false,
    isAllTimeLoading = false,
    datePickerBottomSheetModel = Dashboard.DatePickerBottomSheetModel(
        active = false,
        startDate = "Jan 1",
        endDate = "Jan 2",
    ),
)

/**
 * @author nvoxel
 */
@Composable
internal fun DashboardContentPreview() {
    CanoeTheme {
        DashboardContent(
            model = baseModel,
            onShowDatePickerBottomSheet = {},
            onResetDates = {},
            onDismissRequest = { _, _ -> },
            retryCallback = {},
        )
    }
}

@Composable
internal fun DashboardProjectContentPreview() {
    val model = baseModel.copy(
        projectName = "Canoe",
        summariesModel = baseModel.summariesModel?.copy(
            dailyChartData = baseModel.summariesModel?.dailyChartData?.copy(
                projectsSeries = hashMapOf(),
            )!!,
        ),
    )

    CanoeTheme {
        DashboardContent(
            model = model,
            onShowDatePickerBottomSheet = {},
            onResetDates = {},
            onDismissRequest = { _, _ -> },
            retryCallback = {},
        )
    }
}
