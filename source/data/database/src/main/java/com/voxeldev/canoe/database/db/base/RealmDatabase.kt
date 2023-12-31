package com.voxeldev.canoe.database.db.base

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

/**
 * @author nvoxel
 */
internal abstract class RealmDatabase {

    private var realm: Realm? = null

    abstract val configuration: RealmConfiguration

    fun openRealm(): Realm = realm ?: openRealmInstance()

    fun closeRealm() {
        realm?.close()
        realm = null
    }

    private fun openRealmInstance(): Realm {
        realm = Realm.open(configuration)
        return realm!!
    }
}
