package com.voxeldev.canoe.dashboard.di

import com.voxeldev.canoe.network.di.networkDataModule
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val dashboardFeatureModule = module {

    includes(networkDataModule)
}
