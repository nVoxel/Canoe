package com.voxeldev.canoe.settings.api

/**
 * @author nvoxel
 */
interface TokenRepository {

    fun getAccessToken(): Result<String>

    fun setAccessToken(accessToken: String): Result<Unit>

    fun clearAccessToken(): Result<Unit>

    fun getRefreshToken(): Result<String>

    fun setRefreshToken(refreshToken: String): Result<Unit>

    fun clearRefreshToken(): Result<Unit>
}
