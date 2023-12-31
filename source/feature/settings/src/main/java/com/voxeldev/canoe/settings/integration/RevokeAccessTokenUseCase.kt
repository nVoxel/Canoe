package com.voxeldev.canoe.settings.integration

import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class RevokeAccessTokenUseCase : BaseUseCase<BaseUseCase.NoParams, Unit>(), KoinComponent {

    private val authenticationRepository: AuthenticationRepository by inject()

    override suspend fun run(params: NoParams): Result<Unit> =
        authenticationRepository.revokeAccessToken()
}
