package com.voxeldev.canoe.database.objects

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * @author nvoxel
 */
class ProgramLanguagesObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var timestamp: Long = 0L
    var data: RealmList<ProgramLanguageObject> = realmListOf()
    var total: Int = 0
    var totalPages: Int = 0
}

class ProgramLanguageObject : EmbeddedRealmObject {
    var id: String = ""
    var name: String = ""
    var color: Int = 0
    var isVerified: Boolean? = null
}
