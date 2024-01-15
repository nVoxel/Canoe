package com.voxeldev.canoe.compose.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.Error
import com.voxeldev.canoe.compose.ui.components.Loader
import com.voxeldev.canoe.compose.ui.components.charts.DailyActivityChart
import com.voxeldev.canoe.compose.ui.components.charts.LanguagesChart
import com.voxeldev.canoe.compose.ui.components.charts.ProjectActivityChart
import com.voxeldev.canoe.compose.ui.components.charts.UsageChart
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.integration.DashboardComponent

/**
 * @author nvoxel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardContent(component: DashboardComponent) {
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = model.projectName ?: "Dashboard") },
                actions = {
                    IconButton(onClick = { component.onShowDatePickerBottomSheet() }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Change date range")
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                if (model.isSummariesLoading || model.isProgramLanguagesLoading) Loader()

                model.errorText?.let {
                    Error(
                        message = it,
                        shouldShowRetry = true,
                        retryCallback = component::onReloadClicked,
                    )
                }

                with(model) {
                    summariesModel?.let { summariesModel ->
                        programLanguagesModel?.let { programLanguagesModel ->
                            allTimeModel?.let { allTimeModel ->
                                Header(
                                    summariesModel = summariesModel,
                                    allTimeModel = allTimeModel,
                                    projectName = projectName,
                                    startDate = model.datePickerBottomSheetModel.startDate,
                                    endDate = model.datePickerBottomSheetModel.endDate,
                                )
                            }

                            Charts(
                                summariesModel = summariesModel,
                                programLanguagesModel = programLanguagesModel,
                                displayProjectActivityChart = projectName != null,
                            )
                        }

                        DatePickerBottomSheet(
                            isVisible = model.datePickerBottomSheetModel.active,
                            onDismissRequest = component::onDismissDatePickerBottomSheet,
                        )
                    }
                }
            }
        },
    )
}

@Composable
internal fun Header(
    summariesModel: SummariesModel,
    allTimeModel: AllTimeModel,
    projectName: String?,
    startDate: String,
    endDate: String,
) {
    val headerText = remember(summariesModel) {
        "${summariesModel.cumulativeTotal.text} from $startDate to $endDate.\nDaily average is: ${summariesModel.dailyAverage.text}."
    }
    val allTimeText = remember(allTimeModel, projectName) {
        "Total time spent ${projectName?.let { "for $it " } ?: ""}is: ${allTimeModel.data.text}."
    }

    val headerSpanStyles = remember(summariesModel) {
        listOf(
            AnnotatedString.Range(
                item = SpanStyle(fontWeight = FontWeight.Bold),
                start = 0,
                end = summariesModel.cumulativeTotal.text.length,
            ),
            AnnotatedString.Range(
                item = SpanStyle(fontWeight = FontWeight.Bold),
                start = headerText.length - summariesModel.dailyAverage.text.length - 1,
                end = headerText.length - 1,
            ),
        )
    }
    val allTimeSpanStyles = remember(allTimeModel, projectName) {
        listOf(
            AnnotatedString.Range(
                item = SpanStyle(fontWeight = FontWeight.Bold),
                start = allTimeText.length - allTimeModel.data.text.length - 1,
                end = allTimeText.length - 1,
            ),
        )
    }

    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = AnnotatedString(text = headerText, spanStyles = headerSpanStyles),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        text = AnnotatedString(text = allTimeText, spanStyles = allTimeSpanStyles),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun Charts(
    summariesModel: SummariesModel,
    programLanguagesModel: ProgramLanguagesModel,
    displayProjectActivityChart: Boolean,
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState()),
    ) {
        if (summariesModel.dailyChartData.projectsSeries.isNotEmpty()) {
            ChartCard(title = "Daily Stats") {
                DailyActivityChart(data = summariesModel.dailyChartData)
            }
        }

        if (displayProjectActivityChart) {
            ChartCard(title = "Project Stats") {
                ProjectActivityChart(data = summariesModel.dailyChartData)
            }
        }

        if (summariesModel.languagesChartData.isNotEmpty()) {
            ChartCard(title = "Languages Usage") {
                LanguagesChart(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    data = summariesModel.languagesChartData,
                    programLanguagesModel = programLanguagesModel,
                )
            }
        }

        if (summariesModel.editorsChartData.isNotEmpty()) {
            ChartCard(title = "Editors Usage") {
                UsageChart(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    data = summariesModel.editorsChartData,
                )
            }
        }

        if (summariesModel.operatingSystemsChartData.isNotEmpty()) {
            ChartCard(title = "OS Usage") {
                UsageChart(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    data = summariesModel.operatingSystemsChartData,
                )
            }
        }

        if (summariesModel.machinesChartData.isNotEmpty()) {
            ChartCard(title = "Machines Usage") {
                UsageChart(
                    modifier = Modifier
                        .padding(all = 8.dp),
                    data = summariesModel.machinesChartData,
                )
            }
        }
    }
}

@Composable
private fun ChartCard(
    modifier: Modifier = Modifier,
    title: String,
    chart: @Composable () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(all = 16.dp),
                text = title,
            )
            chart()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerBottomSheet(
    isVisible: Boolean,
    onDismissRequest: (startMillis: Long?, endMillis: Long?) -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismissRequest(
                    dateRangePickerState.selectedStartDateMillis,
                    dateRangePickerState.selectedEndDateMillis,
                )
            },
            sheetState = sheetState,
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = true,
            )
        }
    }
}
