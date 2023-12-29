package com.voxeldev.canoe.local.di

import com.voxeldev.canoe.local.token.TokenRepositoryImpl
import com.voxeldev.canoe.settings.api.TokenRepository
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val localDataModule = module {

    single<TokenRepository> { TokenRepositoryImpl(tokenProvider = get()) }
}
