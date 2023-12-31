package com.voxeldev.canoe.database.db.leaderboards

import com.voxeldev.canoe.database.db.base.RealmDatabase
import com.voxeldev.canoe.database.objects.LeaderboardsObject
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

/**
 * @author nvoxel
 */
internal class DefaultLeaderboardsDatabase : LeaderboardsDatabase, RealmDatabase() {

    override val configuration: RealmConfiguration = super.configuration

    override fun add(realmObject: LeaderboardsObject) {
        openRealm().writeBlocking {
            copyToRealm(realmObject)
        }.also { closeRealm() }
    }

    override fun <R> query(query: String, vararg args: Any?, block: RealmResults<LeaderboardsObject>.() -> R?): R? =
        openRealm().query<LeaderboardsObject>(query = query, args = args).find().block().also { closeRealm() }
}
