package com.voxeldev.canoe.settings.integration

import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetAccessTokenFromCodeUseCase : BaseUseCase<String, Unit>(), KoinComponent {

    private val authenticationRepository: AuthenticationRepository by inject()

    override suspend fun run(params: String): Result<Unit> =
        authenticationRepository.getAccessToken(params)
}
