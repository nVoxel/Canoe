package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.utils.extensions.toColorInt

/**
 * @author nvoxel
 */
@Composable
internal fun LanguagesChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Pair<Float, String>>>,
    programLanguagesModel: ProgramLanguagesModel,
) {
    UsageChart(
        modifier = modifier,
        data = data.applyColors(programLanguagesModel),
        isDonutChart = false
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun UsageChart(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Triple<Float, String, Int>>>,
    isDonutChart: Boolean = true,
) {
    val pieChartData = PieChartData(
        slices = data.map {
            PieChartData.Slice(
                label = it.first,
                value = it.second.first,
                color = Color(color = it.second.third),
            )
        },
        plotType = if (isDonutChart) PlotType.Donut else PlotType.Pie,
    )

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        chartPadding = 25,
        backgroundColor = Color.Transparent,
    )

    Column {
        if (isDonutChart) {
            DonutPieChart(
                modifier = modifier,
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig,
            )
        } else {
            PieChart(
                modifier = modifier,
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig,
            )
        }

        FlowRow(
            modifier = Modifier
                .padding(all = 16.dp),
        ) {
            data.forEach {
                SuggestionChip(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    onClick = {},
                    label = { Text(text = "${it.first}: ${it.second.second}") },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(size = 16.dp)
                                .background(color = Color(color = it.second.third))
                        )
                    }
                )
            }
        }
    }
}

private fun List<Pair<String, Pair<Float, String>>>.applyColors(programLanguagesModel: ProgramLanguagesModel) =
    map { entry ->
        val colorInt = programLanguagesModel.data.firstOrNull { programLanguage -> programLanguage.name == entry.first }?.color
        entry.first to Triple(entry.second.first, entry.second.second, colorInt ?: entry.first.toColorInt())
    }
