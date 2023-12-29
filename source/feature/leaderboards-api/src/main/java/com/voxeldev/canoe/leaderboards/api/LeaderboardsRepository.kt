package com.voxeldev.canoe.leaderboards.api

/**
 * @author nvoxel
 */
interface LeaderboardsRepository {

    suspend fun getLeaderboards(request: LeaderboardsRequest): Result<LeaderboardsModel>
}
