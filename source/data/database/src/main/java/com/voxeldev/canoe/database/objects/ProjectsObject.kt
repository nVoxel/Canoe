package com.voxeldev.canoe.database.objects

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

/**
 * @author nvoxel
 */
class ProjectsObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var timestamp: Long = 0L
    var data: RealmList<ProjectObject> = realmListOf()
    var query: String? = null
    var message: String? = null
}

class ProjectObject : EmbeddedRealmObject {
    var id: String = ""
    var name: String = ""
    var repository: String? = ""
    var badge: String? = ""
    var color: String? = ""
    var hasPublicUrl: Boolean = false
    var humanReadableLastHeartBeatAt: String? = ""
    var lastHeartBeatAt: String? = ""
    var url: String = ""
    var urlEncodedName: String = ""
    var createdAt: String = ""
}
