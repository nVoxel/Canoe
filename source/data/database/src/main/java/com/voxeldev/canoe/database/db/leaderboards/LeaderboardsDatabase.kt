package com.voxeldev.canoe.database.db.leaderboards

import com.voxeldev.canoe.database.db.base.CrDatabase
import com.voxeldev.canoe.database.objects.CityObject
import com.voxeldev.canoe.database.objects.CurrentUserObject
import com.voxeldev.canoe.database.objects.LanguageObject
import com.voxeldev.canoe.database.objects.LeaderboardEntryObject
import com.voxeldev.canoe.database.objects.LeaderboardsObject
import com.voxeldev.canoe.database.objects.RunningTotalObject
import com.voxeldev.canoe.database.objects.TimeRangeObject
import com.voxeldev.canoe.database.objects.UserObject
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
interface LeaderboardsDatabase : CrDatabase<LeaderboardsObject> {

    override val configuration: RealmConfiguration
        get() = RealmConfiguration.create(
            schema = setOf(
                LeaderboardsObject::class,
                CurrentUserObject::class,
                UserObject::class,
                CityObject::class,
                LeaderboardEntryObject::class,
                RunningTotalObject::class,
                TimeRangeObject::class,
                LanguageObject::class,
            ),
        )
}
