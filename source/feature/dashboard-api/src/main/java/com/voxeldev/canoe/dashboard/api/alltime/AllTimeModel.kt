package com.voxeldev.canoe.dashboard.api.alltime

/**
 * @author nvoxel
 */
data class AllTimeModel(
    val data: AllTimeData,
    val message: String?,
)

data class AllTimeData(
    val decimal: String,
    val digital: String,
    val text: String,
    val totalSeconds: Float,
)
