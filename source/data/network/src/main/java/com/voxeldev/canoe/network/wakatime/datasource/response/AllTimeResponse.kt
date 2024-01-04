package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class AllTimeResponse(
    val data: AllTimeResponseData,
    val message: String? = null,
)

@Serializable
internal data class AllTimeResponseData(
    val decimal: String,
    val digital: String,
    val text: String,
    @SerialName("total_seconds")
    val totalSeconds: Float,
)
