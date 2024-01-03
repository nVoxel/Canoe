package com.voxeldev.canoe.dashboard.api.sumaries

/**
 * @author nvoxel
 */
data class SummariesModel(
    val dailyChartData: DailyChartData,
    val languagesChartData: List<Pair<String, Pair<Float, String>>>,
    val editorsChartData: List<Pair<String, Triple<Float, String, Int>>>,
    val operatingSystemsChartData: List<Pair<String, Triple<Float, String, Int>>>,
    val machinesChartData: List<Pair<String, Triple<Float, String, Int>>>,
    val cumulativeTotal: CumulativeTotal,
    val dailyAverage: DailyAverage,
)

data class DailyChartData(
    val projectsSeries: HashMap<String, MutableList<Pair<Float, String>>>,
    val totalLabels: MutableList<Pair<Float, String>>,
    val horizontalLabels: MutableList<String>,
)

data class GrandTotal(
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val text: String,
    val totalSeconds: Float,
)

data class Range(
    val date: String,
    val text: String,
)

data class CumulativeTotal(
    val seconds: Float,
    val text: String,
    val digital: String,
)

data class DailyAverage(
    val seconds: Float,
    val text: String,
)

const val DEFAULT_EMPTY_VALUE = .000001f
