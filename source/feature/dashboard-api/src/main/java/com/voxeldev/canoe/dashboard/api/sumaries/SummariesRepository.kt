package com.voxeldev.canoe.dashboard.api.sumaries

/**
 * @author nvoxel
 */
interface SummariesRepository {

    suspend fun getSummaries(request: SummariesRequest): Result<SummariesModel>
}
