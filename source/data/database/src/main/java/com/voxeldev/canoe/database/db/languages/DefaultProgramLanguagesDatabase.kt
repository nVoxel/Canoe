package com.voxeldev.canoe.database.db.languages

import com.voxeldev.canoe.database.db.base.RealmDatabase
import com.voxeldev.canoe.database.objects.ProgramLanguagesObject
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

/**
 * @author nvoxel
 */
internal class DefaultProgramLanguagesDatabase : ProgramLanguagesDatabase, RealmDatabase() {

    override val configuration: RealmConfiguration = super.configuration

    override fun add(realmObject: ProgramLanguagesObject) {
        openRealm().writeBlocking {
            copyToRealm(realmObject)
        }.also { closeRealm() }
    }

    override fun <R> query(query: String, vararg args: Any?, block: RealmResults<ProgramLanguagesObject>.() -> R?): R? =
        openRealm().query<ProgramLanguagesObject>(query = query, args = args).find().block().also { closeRealm() }
}
