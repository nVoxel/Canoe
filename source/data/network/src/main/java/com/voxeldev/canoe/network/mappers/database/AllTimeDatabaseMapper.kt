package com.voxeldev.canoe.network.mappers.database

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeData
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.database.objects.AllTimeObject
import com.voxeldev.canoe.database.objects.AllTimeObjectData
import com.voxeldev.canoe.network.wakatime.datasource.response.AllTimeResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.AllTimeResponseData

/**
 * @author nvoxel
 */
internal class AllTimeDatabaseMapper {

    fun toObject(allTimeResponse: AllTimeResponse, projectName: String?): AllTimeObject =
        AllTimeObject().apply {
            timestamp = System.currentTimeMillis() / 1000
            project = projectName
            data = toObject(allTimeResponse.data)
            message = allTimeResponse.message
        }

    private fun toObject(allTimeResponseData: AllTimeResponseData): AllTimeObjectData =
        AllTimeObjectData().apply {
            decimal = allTimeResponseData.decimal
            digital = allTimeResponseData.digital
            text = allTimeResponseData.text
            totalSeconds = allTimeResponseData.totalSeconds
        }

    fun toModel(allTimeObject: AllTimeObject): AllTimeModel =
        AllTimeModel(
            data = allTimeObject.data?.toModel() ?: defaultAllTimeData,
            message = allTimeObject.message,
        )

    fun AllTimeObjectData.toModel(): AllTimeData =
        AllTimeData(
            decimal = decimal,
            digital = digital,
            text = text,
            totalSeconds = totalSeconds,
        )

    private companion object {
        val defaultAllTimeData = AllTimeData(
            decimal = "0",
            digital = "0",
            text = "0",
            totalSeconds = 0f,
        )
    }
}
