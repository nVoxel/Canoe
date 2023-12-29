package com.voxeldev.canoe.utils.providers.token

/**
 * @author nvoxel
 */
interface TokenProvider {
    fun getAccessToken(): String?
    fun setAccessToken(accessToken: String)
    fun clearAccessToken()
    fun getRefreshToken(): String?
    fun setRefreshToken(refreshToken: String)
    fun clearRefreshToken()
}
