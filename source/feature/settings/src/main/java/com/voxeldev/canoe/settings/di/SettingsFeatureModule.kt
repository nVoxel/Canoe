package com.voxeldev.canoe.settings.di

import com.voxeldev.canoe.local.di.localDataModule
import com.voxeldev.canoe.network.di.networkDataModule
import com.voxeldev.canoe.utils.di.utilsModule
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val settingsFeatureModule = module {

    includes(localDataModule, networkDataModule, utilsModule)
}
