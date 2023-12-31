package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class AuthenticationResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)
