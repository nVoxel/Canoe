package com.voxeldev.canoe.projects.di

import com.voxeldev.canoe.network.di.networkDataModule
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val projectsFeatureModule = module {

    includes(networkDataModule)
}
