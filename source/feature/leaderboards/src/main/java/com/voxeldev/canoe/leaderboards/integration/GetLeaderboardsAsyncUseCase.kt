package com.voxeldev.canoe.leaderboards.integration

import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRepository
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetLeaderboardsAsyncUseCase : BaseUseCase<LeaderboardsRequest, LeaderboardsModel>(), KoinComponent {

    private val leaderboardsRepository: LeaderboardsRepository by inject()

    override suspend fun run(params: LeaderboardsRequest): Result<LeaderboardsModel> =
        leaderboardsRepository.getLeaderboards(params)
}
