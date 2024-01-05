package com.voxeldev.canoe.database.db.alltime

import com.voxeldev.canoe.database.db.base.RealmDatabase
import com.voxeldev.canoe.database.objects.AllTimeObject
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

/**
 * @author nvoxel
 */
internal class DefaultAllTimeDatabase : AllTimeDatabase, RealmDatabase() {

    override val configuration: RealmConfiguration = super.configuration

    override fun add(realmObject: AllTimeObject) {
        openRealm().writeBlocking {
            copyToRealm(realmObject)
        }.also { closeRealm() }
    }

    override fun <R> query(query: String, vararg args: Any?, block: RealmResults<AllTimeObject>.() -> R?): R? =
        openRealm().query<AllTimeObject>(query = query, args = args).find().block().also { closeRealm() }
}
