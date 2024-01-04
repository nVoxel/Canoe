package com.voxeldev.canoe.network.di

import com.voxeldev.canoe.network.mappers.database.AllTimeDatabaseMapper
import com.voxeldev.canoe.network.mappers.database.LeaderboardsDatabaseMapper
import com.voxeldev.canoe.network.mappers.database.ProgramLanguagesDatabaseMapper
import com.voxeldev.canoe.network.mappers.database.ProjectsDatabaseMapper
import com.voxeldev.canoe.network.mappers.database.SummariesDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.AllTimeMapper
import com.voxeldev.canoe.network.mappers.network.LeaderboardsMapper
import com.voxeldev.canoe.network.mappers.network.ProgramLanguagesMapper
import com.voxeldev.canoe.network.mappers.network.ProjectsMapper
import com.voxeldev.canoe.network.mappers.network.SummariesMapper
import org.koin.dsl.module

/**
 * @author nvoxel
 */
val mappersModule = module {

    single { AllTimeMapper() }
    single { AllTimeDatabaseMapper() }

    single { LeaderboardsMapper() }
    single { LeaderboardsDatabaseMapper() }

    single { ProgramLanguagesMapper() }
    single { ProgramLanguagesDatabaseMapper() }

    single { ProjectsMapper() }
    single { ProjectsDatabaseMapper() }

    single { SummariesMapper() }
    single { SummariesDatabaseMapper() }
}
