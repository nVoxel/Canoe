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
class LeaderboardsObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var timestamp: Long = 0L
    var currentUser: CurrentUserObject? = CurrentUserObject()
    var data: RealmList<LeaderboardEntryObject> = realmListOf()
    var page: Int = 0
    var totalPages: Int = 0
    var range: TimeRangeObject? = TimeRangeObject()
    var language: String? = null
    var isHireable: Boolean? = null
    var countryCode: String? = null
    var modifiedAt: String = ""
    var timeout: Int = 0
    var writesOnly: Boolean = false
}

class CurrentUserObject : EmbeddedRealmObject {
    var rank: Int? = null
    var runningTotal: RunningTotalObject? = null
    var page: Int? = null
    var user: UserObject? = UserObject()
}

class UserObject : EmbeddedRealmObject {
    var id: String = ""
    var email: String? = null
    var username: String? = null
    var fullName: String? = null
    var displayName: String? = null
    var website: String? = null
    var humanReadableWebsite: String? = null
    var isHireable: Boolean? = null
    var city: CityObject? = null
    var isEmailPublic: Boolean? = null
    var photoPublic: Boolean? = null
}

class CityObject : EmbeddedRealmObject {
    var countryCode: String? = null
    var name: String? = null
    var state: String? = null
    var title: String? = null
}

class LeaderboardEntryObject : EmbeddedRealmObject {
    var rank: Int = 0
    var runningTotal: RunningTotalObject? = null
    var user: UserObject? = UserObject()
}

class RunningTotalObject : EmbeddedRealmObject {
    var totalSeconds: Float? = null
    var humanReadableTotal: String? = null
    var dailyAverage: Float? = null
    var humanReadableDailyAverage: String? = null
    var languages: RealmList<LanguageObject>? = null
}

class TimeRangeObject : EmbeddedRealmObject {
    var startDate: String? = null
    var startText: String? = null
    var endDate: String? = null
    var endText: String? = null
    var name: String? = null
    var text: String? = null
}
