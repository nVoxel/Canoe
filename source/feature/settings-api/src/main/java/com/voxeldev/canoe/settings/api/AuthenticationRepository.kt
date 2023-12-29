package com.voxeldev.canoe.settings.api

import io.ktor.client.request.HttpRequestBuilder

/**
 * @author nvoxel
 */
interface AuthenticationRepository {

    suspend fun getAccessToken(authorizationCode: String): Result<Unit>

    suspend fun refreshAccessToken(block: HttpRequestBuilder.() -> Unit = {}): Result<Unit>

    suspend fun revokeAccessToken(): Result<Unit>
}
