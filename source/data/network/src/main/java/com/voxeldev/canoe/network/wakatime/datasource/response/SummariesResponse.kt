package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class SummariesResponse(
    val data: List<SummariesDayResponse>,
    @SerialName("cumulative_total")
    val cumulativeTotal: CumulativeTotalResponse,
    @SerialName("daily_average")
    val dailyAverage: DailyAverageResponse,
    val start: String,
    val end: String,
)

@Serializable
internal data class SummariesDayResponse(
    @SerialName("grand_total")
    val grandTotal: GrandTotalResponse,
    val categories: List<CategoryResponse>,
    val projects: List<SummaryProjectResponse>? = null,
    val languages: List<LanguageResponse>,
    val editors: List<EditorResponse>,
    @SerialName("operating_systems")
    val operatingSystems: List<OperatingSystemResponse>,
    val dependencies: List<DependencyResponse>,
    val machines: List<MachineResponse>,
    val branches: List<BranchResponse>? = null,
    val entities: List<EntityResponse>? = null,
    val range: RangeResponse,
)

@Serializable
internal sealed interface GeneralizedEntityResponse {
    val name: String

    @SerialName("total_seconds")
    val totalSeconds: Float
}

@Serializable
internal data class GrandTotalResponse(
    val digital: String,
    val hours: Int,
    val minutes: Int,
    val text: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
)

@Serializable
internal data class CategoryResponse(
    val name: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
)

@Serializable
internal data class SummaryProjectResponse(
    val name: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val decimal: Float,
    val text: String,
    val hours: Int,
    val minutes: Int,
)

@Serializable
internal data class LanguageResponse(
    override val name: String,
    @SerialName("total_seconds")
    override val totalSeconds: Float,
    val percent: Float? = null,
    val digital: String? = null,
    val text: String? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val seconds: Int? = null,
) : GeneralizedEntityResponse

@Serializable
internal data class EditorResponse(
    override val name: String,
    @SerialName("total_seconds")
    override val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
) : GeneralizedEntityResponse

@Serializable
internal data class OperatingSystemResponse(
    override val name: String,
    @SerialName("total_seconds")
    override val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
) : GeneralizedEntityResponse

@Serializable
internal data class DependencyResponse(
    val name: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)

@Serializable
internal data class MachineResponse(
    override val name: String,
    @SerialName("total_seconds")
    override val totalSeconds: Float,
    @SerialName("machine_name_id")
    val machineNameId: String,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
) : GeneralizedEntityResponse

@Serializable
internal data class BranchResponse(
    val name: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)

@Serializable
internal data class EntityResponse(
    val name: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
    val percent: Float,
    val digital: String,
    val text: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)

@Serializable
internal data class RangeResponse(
    val date: String,
    val start: String,
    val end: String,
    val text: String,
    val timezone: String,
)

@Serializable
internal data class CumulativeTotalResponse(
    val seconds: Float,
    val text: String,
    val decimal: String,
    val digital: String,
)

@Serializable
internal data class DailyAverageResponse(
    val holidays: Int,
    @SerialName("days_including_holidays")
    val daysIncludingHolidays: Int,
    @SerialName("days_minus_holidays")
    val daysMinusHolidays: Int,
    val seconds: Float,
    val text: String,
    @SerialName("seconds_including_other_language")
    val secondsIncludingOtherLanguage: Float,
    @SerialName("text_including_other_language")
    val textIncludingOtherLanguage: String,
)
