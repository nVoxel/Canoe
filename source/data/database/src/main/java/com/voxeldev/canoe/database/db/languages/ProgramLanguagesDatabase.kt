package com.voxeldev.canoe.database.db.languages

import com.voxeldev.canoe.database.db.base.CrDatabase
import com.voxeldev.canoe.database.objects.ProgramLanguageObject
import com.voxeldev.canoe.database.objects.ProgramLanguagesObject
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
interface ProgramLanguagesDatabase : CrDatabase<ProgramLanguagesObject> {

    override val configuration: RealmConfiguration
        get() = RealmConfiguration.create(
            schema = setOf(
                ProgramLanguagesObject::class,
                ProgramLanguageObject::class,
            ),
        )
}
