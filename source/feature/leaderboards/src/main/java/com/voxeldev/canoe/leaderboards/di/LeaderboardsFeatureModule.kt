package com.voxeldev.canoe.leaderboards.di

import com.voxeldev.canoe.network.di.networkDataModule
import com.voxeldev.canoe.utils.di.utilsModule
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val leaderboardsFeatureModule = module {

    includes(networkDataModule, utilsModule)
}
