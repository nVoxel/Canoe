package com.voxeldev.canoe.settings.integration

import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetAccessTokenFromStorageUseCase : BaseUseCase<BaseUseCase.NoParams, String>(), KoinComponent {

    private val tokenRepository: TokenRepository by inject()

    override suspend fun run(params: NoParams): Result<String> =
        tokenRepository.getAccessToken()
}
