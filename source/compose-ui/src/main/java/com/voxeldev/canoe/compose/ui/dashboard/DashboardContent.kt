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
import com.voxeldev.canoe.compose.ui.components.charts.UsageChart
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.integration.DashboardComponent

/**
 * @author nvoxel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(component: DashboardComponent) {
    val model by component.model.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dashboard") },
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
                    .padding(paddingValues = paddingValues),
            ) {
                if (model.isSummariesLoading || model.isProgramLanguagesLoading) Loader()

                with(model) {
                    summariesModel?.let { summariesModel ->
                        programLanguagesModel?.let { programLanguagesModel ->
                            Header(
                                summariesModel = summariesModel,
                                startDate = model.datePickerBottomSheetModel.startDate,
                                endDate = model.datePickerBottomSheetModel.endDate,
                            )

                            Charts(summariesModel = summariesModel, programLanguagesModel = programLanguagesModel)
                        }

                        DatePickerBottomSheet(
                            isVisible = model.datePickerBottomSheetModel.active,
                            onDismissRequest = component::onDismissDatePickerBottomSheet,
                        )
                    } ?: run {
                        errorText?.let {
                            Error(
                                message = it,
                                shouldShowRetry = true,
                                retryCallback = component::onReloadClicked
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun Header(
    summariesModel: SummariesModel,
    startDate: String,
    endDate: String,
) {
    val headerText =
        "${summariesModel.cumulativeTotal.text} from $startDate to $endDate.\nDaily average is: ${summariesModel.dailyAverage.text}."

    val spanStyles = listOf(
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

    Text(
        modifier = Modifier.padding(all = 16.dp),
        text = AnnotatedString(text = headerText, spanStyles = spanStyles),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun Charts(summariesModel: SummariesModel, programLanguagesModel: ProgramLanguagesModel) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
    ) {
        ChartCard(title = "Daily Stats") {
            DailyActivityChart(data = summariesModel.dailyChartData)
        }

        ChartCard(title = "Languages Usage") {
            LanguagesChart(
                modifier = Modifier
                    .padding(all = 8.dp),
                data = summariesModel.languagesChartData,
                programLanguagesModel = programLanguagesModel,
            )
        }

        ChartCard(title = "Editors Usage") {
            UsageChart(
                modifier = Modifier
                    .padding(all = 8.dp),
                data = summariesModel.editorsChartData,
            )
        }

        ChartCard(title = "OS Usage") {
            UsageChart(
                modifier = Modifier
                    .padding(all = 8.dp),
                data = summariesModel.operatingSystemsChartData,
            )
        }

        ChartCard(title = "Machines Usage") {
            UsageChart(
                modifier = Modifier
                    .padding(all = 8.dp),
                data = summariesModel.machinesChartData,
            )
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
                    dateRangePickerState.selectedEndDateMillis
                )
            },
            sheetState = sheetState
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = true,
            )
        }
    }
}
