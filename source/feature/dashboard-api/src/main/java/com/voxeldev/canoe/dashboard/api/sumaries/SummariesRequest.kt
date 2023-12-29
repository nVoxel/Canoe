package com.voxeldev.canoe.dashboard.api.sumaries

/**
 * @author nvoxel
 */
data class SummariesRequest(
    val startDate: String,
    val endDate: String,
    val project: String? = null,
    val branches: String? = null,
    val timeout: Int? = null,
    val writesOnly: Boolean? = null,
    val timeZone: String? = null,
)
