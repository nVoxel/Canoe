package com.voxeldev.canoe.compose.ui.components.charts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.lineSpec
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.model.CartesianChartModel
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData

/**
 * @author nvoxel
 */
@Composable
internal fun ProjectActivityChart(data: DailyChartData) {
    val chartModel = remember(data) {
        CartesianChartModel(
            LineCartesianLayerModel.build {
                series(data.totalLabels.map { it.first })
            },
        )
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lines = listOf(
                    lineSpec(
                        shader = DynamicShaders.color(color = MaterialTheme.colorScheme.primary),
                    ),
                ),
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _, _ ->
                    data.horizontalLabels[value.toInt()]
                },
            ),
        ),
        model = chartModel,
        marker = rememberProjectMarker(totalLabels = data.totalLabels),
        isZoomEnabled = false,
    )
}
