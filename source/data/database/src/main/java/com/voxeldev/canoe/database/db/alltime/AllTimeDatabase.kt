package com.voxeldev.canoe.database.db.alltime

import com.voxeldev.canoe.database.db.base.CrDatabase
import com.voxeldev.canoe.database.objects.AllTimeObject
import com.voxeldev.canoe.database.objects.AllTimeObjectData
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
interface AllTimeDatabase : CrDatabase<AllTimeObject> {

    override val configuration: RealmConfiguration
        get() = RealmConfiguration.create(
            schema = setOf(
                AllTimeObject::class,
                AllTimeObjectData::class,
            ),
        )
}
