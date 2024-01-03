package com.voxeldev.canoe.database.db.summaries

import com.voxeldev.canoe.database.db.base.CrDatabase
import com.voxeldev.canoe.database.objects.BranchObject
import com.voxeldev.canoe.database.objects.CategoryObject
import com.voxeldev.canoe.database.objects.CumulativeTotalObject
import com.voxeldev.canoe.database.objects.DailyAverageObject
import com.voxeldev.canoe.database.objects.DependencyObject
import com.voxeldev.canoe.database.objects.EditorObject
import com.voxeldev.canoe.database.objects.GrandTotalObject
import com.voxeldev.canoe.database.objects.LanguageObject
import com.voxeldev.canoe.database.objects.MachineObject
import com.voxeldev.canoe.database.objects.OperatingSystemObject
import com.voxeldev.canoe.database.objects.RangeObject
import com.voxeldev.canoe.database.objects.SummariesDayObject
import com.voxeldev.canoe.database.objects.SummariesObject
import com.voxeldev.canoe.database.objects.SummaryProjectObject
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
interface SummariesDatabase : CrDatabase<SummariesObject> {

    override val configuration: RealmConfiguration
        get() = RealmConfiguration.create(
            schema = setOf(
                SummariesObject::class,
                SummariesDayObject::class,
                GrandTotalObject::class,
                CategoryObject::class,
                SummaryProjectObject::class,
                LanguageObject::class,
                EditorObject::class,
                OperatingSystemObject::class,
                DependencyObject::class,
                MachineObject::class,
                BranchObject::class,
                RangeObject::class,
                CumulativeTotalObject::class,
                DailyAverageObject::class,
            ),
        )
}
