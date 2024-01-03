package com.voxeldev.canoe.database.db.projects

import com.voxeldev.canoe.database.db.base.CrDatabase
import com.voxeldev.canoe.database.objects.ProjectObject
import com.voxeldev.canoe.database.objects.ProjectsObject
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
interface ProjectsDatabase : CrDatabase<ProjectsObject> {

    override val configuration: RealmConfiguration
        get() = RealmConfiguration.create(
            schema = setOf(
                ProjectsObject::class,
                ProjectObject::class,
            ),
        )
}
