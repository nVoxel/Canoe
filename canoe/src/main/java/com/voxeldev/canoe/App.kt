package com.voxeldev.canoe

import android.app.Application
import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.leaderboards.di.leaderboardsFeatureModule
import com.voxeldev.canoe.projects.di.projectsFeatureModule
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author nvoxel
 */
internal class App : Application() {

    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(dashboardFeatureModule, leaderboardsFeatureModule, projectsFeatureModule, settingsFeatureModule)
        }

        super.onCreate()
    }
}
