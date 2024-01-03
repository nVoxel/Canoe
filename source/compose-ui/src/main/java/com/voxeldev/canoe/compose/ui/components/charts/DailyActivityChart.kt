package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.core.chart.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.model.CartesianChartModel
import com.patrykandpatrick.vico.core.model.ColumnCartesianLayerModel
import com.voxeldev.canoe.compose.ui.theme.LocalChartColorsPalette
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData

/**
 * @author nvoxel
 */
@Composable
internal fun DailyActivityChart(data: DailyChartData) {
    val chartModel = remember(data) {
        CartesianChartModel(
            ColumnCartesianLayerModel.build {
                data.projectsSeries.values.forEach { day ->
                    series(day.map { it.first })
                }
            },
        )
    }

    val columns: List<LineComponent> = listOf(
        rememberLineComponent(color = LocalChartColorsPalette.current.red, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.green, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.yellow, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.blue, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.cyan, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.orange, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.magenta, thickness = 16.dp),
        rememberLineComponent(color = LocalChartColorsPalette.current.purple, thickness = 16.dp),
    )

    val columnLayer = rememberColumnCartesianLayer(
        columns = columns,
        spacing = 44.dp,
        mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
    )

    CartesianChartHost(
        modifier = Modifier.height(250.dp),
        chart =
        rememberCartesianChart(
            columnLayer,
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _, _ ->
                    data.horizontalLabels[value.toInt()]
                },
            ),
        ),
        model = chartModel,
        marker = rememberDailyMarker(
            data.projectsSeries.values.toList(),
            data.totalLabels,
        ),
        isZoomEnabled = false,
    )
}
