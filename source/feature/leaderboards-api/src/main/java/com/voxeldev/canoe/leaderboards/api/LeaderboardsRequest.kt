package com.voxeldev.canoe.leaderboards.api

/**
 * @author nvoxel
 */
data class LeaderboardsRequest(
    val language: String? = null,
    val isHireable: Boolean? = null,
    val countryCode: String? = null,
    val page: Int = 1,
)
