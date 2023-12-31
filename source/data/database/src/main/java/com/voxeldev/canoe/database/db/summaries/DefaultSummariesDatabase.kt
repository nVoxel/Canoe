package com.voxeldev.canoe.database.db.summaries

import com.voxeldev.canoe.database.db.base.RealmDatabase
import com.voxeldev.canoe.database.objects.SummariesObject
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

/**
 * @author nvoxel
 */
internal class DefaultSummariesDatabase : SummariesDatabase, RealmDatabase() {

    override val configuration: RealmConfiguration = super.configuration

    override fun add(realmObject: SummariesObject) {
        openRealm().writeBlocking {
            copyToRealm(realmObject)
        }.also { closeRealm() }
    }

    override fun <R> query(query: String, vararg args: Any?, block: RealmResults<SummariesObject>.() -> R?): R? =
        openRealm().query<SummariesObject>(query = query, args = args).find().block().also { closeRealm() }
}
