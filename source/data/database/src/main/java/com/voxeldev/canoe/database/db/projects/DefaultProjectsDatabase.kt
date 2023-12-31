package com.voxeldev.canoe.database.db.projects

import com.voxeldev.canoe.database.db.base.RealmDatabase
import com.voxeldev.canoe.database.objects.ProjectsObject
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

/**
 * @author nvoxel
 */
internal class DefaultProjectsDatabase : ProjectsDatabase, RealmDatabase() {

    override val configuration: RealmConfiguration = super.configuration

    override fun add(realmObject: ProjectsObject) {
        openRealm().writeBlocking {
            copyToRealm(realmObject)
        }.also { closeRealm() }
    }

    override fun <R> query(query: String, vararg args: Any?, block: RealmResults<ProjectsObject>.() -> R?): R? =
        openRealm().query<ProjectsObject>(query = query, args = args).find().block().also { closeRealm() }
}
