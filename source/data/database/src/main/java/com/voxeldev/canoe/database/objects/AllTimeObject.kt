package com.voxeldev.canoe.database.objects

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * @author nvoxel
 */
class AllTimeObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var timestamp: Long = 0L
    var project: String? = null
    var data: AllTimeObjectData? = AllTimeObjectData()
    var message: String? = null
}

class AllTimeObjectData : EmbeddedRealmObject {
    var decimal: String = ""
    var digital: String = ""
    var text: String = ""
    var totalSeconds: Float = 0f
}
