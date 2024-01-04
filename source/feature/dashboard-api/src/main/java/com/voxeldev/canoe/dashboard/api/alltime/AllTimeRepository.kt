package com.voxeldev.canoe.dashboard.api.alltime

/**
 * @author nvoxel
 */
interface AllTimeRepository {

    suspend fun getAllTime(request: AllTimeRequest): Result<AllTimeModel>
}
