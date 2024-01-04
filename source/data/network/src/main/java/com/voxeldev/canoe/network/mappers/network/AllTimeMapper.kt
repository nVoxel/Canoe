package com.voxeldev.canoe.network.mappers.network

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeData
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.network.wakatime.datasource.response.AllTimeResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.AllTimeResponseData

/**
 * @author nvoxel
 */
internal class AllTimeMapper {

    fun toModel(allTimeResponse: AllTimeResponse): AllTimeModel =
        AllTimeModel(
            data = allTimeResponse.data.toModel(),
            message = allTimeResponse.message,
        )

    private fun AllTimeResponseData.toModel(): AllTimeData =
        AllTimeData(
            decimal = decimal,
            digital = digital,
            text = text,
            totalSeconds = totalSeconds,
        )
}
