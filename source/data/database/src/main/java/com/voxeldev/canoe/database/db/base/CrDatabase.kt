package com.voxeldev.canoe.database.db.base

import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.BaseRealmObject

/**
 * @author nvoxel
 */
interface CrDatabase<T : BaseRealmObject> {

    val configuration: RealmConfiguration

    fun add(realmObject: T)

    fun <R> query(query: String, vararg args: Any?, block: RealmResults<T>.() -> R?): R?
}
