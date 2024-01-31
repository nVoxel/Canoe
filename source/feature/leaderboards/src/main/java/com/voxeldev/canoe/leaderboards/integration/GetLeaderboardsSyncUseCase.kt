package com.voxeldev.canoe.leaderboards.integration

import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRepository
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
class GetLeaderboardsSyncUseCase : KoinComponent {

    private val leaderboardsRepository: LeaderboardsRepository by inject()

    suspend operator fun invoke(params: LeaderboardsRequest): Result<LeaderboardsModel> =
        leaderboardsRepository.getLeaderboards(
            request = params,
        )
}
