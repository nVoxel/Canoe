package com.voxeldev.canoe.database.di

import com.voxeldev.canoe.database.db.alltime.AllTimeDatabase
import com.voxeldev.canoe.database.db.alltime.DefaultAllTimeDatabase
import com.voxeldev.canoe.database.db.languages.DefaultProgramLanguagesDatabase
import com.voxeldev.canoe.database.db.languages.ProgramLanguagesDatabase
import com.voxeldev.canoe.database.db.leaderboards.DefaultLeaderboardsDatabase
import com.voxeldev.canoe.database.db.leaderboards.LeaderboardsDatabase
import com.voxeldev.canoe.database.db.projects.DefaultProjectsDatabase
import com.voxeldev.canoe.database.db.projects.ProjectsDatabase
import com.voxeldev.canoe.database.db.summaries.DefaultSummariesDatabase
import com.voxeldev.canoe.database.db.summaries.SummariesDatabase
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val databaseDataModule = module {

    single<AllTimeDatabase> {
        DefaultAllTimeDatabase()
    }

    single<ProgramLanguagesDatabase> {
        DefaultProgramLanguagesDatabase()
    }

    single<LeaderboardsDatabase> {
        DefaultLeaderboardsDatabase()
    }

    single<ProjectsDatabase> {
        DefaultProjectsDatabase()
    }

    single<SummariesDatabase> {
        DefaultSummariesDatabase()
    }
}
