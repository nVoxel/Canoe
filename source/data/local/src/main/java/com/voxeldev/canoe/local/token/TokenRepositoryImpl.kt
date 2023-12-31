package com.voxeldev.canoe.local.token

import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.utils.exceptions.TokenNotFoundException
import com.voxeldev.canoe.utils.providers.token.TokenProvider

/**
 * @author nvoxel
 */
internal class TokenRepositoryImpl(
    private val tokenProvider: TokenProvider,
) : TokenRepository {

    override fun getAccessToken(): Result<String> =
        runCatching {
            tokenProvider.getAccessToken() ?: throw TokenNotFoundException()
        }

    override fun setAccessToken(accessToken: String): Result<Unit> =
        runCatching {
            tokenProvider.setAccessToken(accessToken)
        }

    override fun clearAccessToken(): Result<Unit> =
        runCatching {
            tokenProvider.clearAccessToken()
        }

    override fun getRefreshToken(): Result<String> =
        runCatching {
            tokenProvider.getRefreshToken() ?: throw TokenNotFoundException()
        }

    override fun setRefreshToken(refreshToken: String): Result<Unit> =
        runCatching {
            tokenProvider.setRefreshToken(refreshToken)
        }

    override fun clearRefreshToken(): Result<Unit> =
        runCatching {
            tokenProvider.clearRefreshToken()
        }
}
