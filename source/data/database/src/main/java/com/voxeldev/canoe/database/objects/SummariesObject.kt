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
class SummariesObject : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var timestamp: Long = 0L
    var project: String? = null
    var data: RealmList<SummariesDayObject> = realmListOf()
    var cumulativeTotal: CumulativeTotalObject? = CumulativeTotalObject()
    var dailyAverage: DailyAverageObject? = DailyAverageObject()
    var start: Long = 0L
    var end: Long = 0L
}

class SummariesDayObject : EmbeddedRealmObject {
    var grandTotal: GrandTotalObject? = GrandTotalObject()
    var categories: RealmList<CategoryObject> = realmListOf()
    var projects: RealmList<SummaryProjectObject> = realmListOf()
    var languages: RealmList<LanguageObject> = realmListOf()
    var editors: RealmList<EditorObject> = realmListOf()
    var operatingSystems: RealmList<OperatingSystemObject> = realmListOf()
    var dependencies: RealmList<DependencyObject> = realmListOf()
    var machines: RealmList<MachineObject> = realmListOf()
    var range: RangeObject? = RangeObject()
}

sealed interface GeneralizedEntityObject {
    val name: String
    val totalSeconds: Float
}

class GrandTotalObject : EmbeddedRealmObject {
    var digital: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var text: String = ""
    var totalSeconds: Float = 0f
}

class CategoryObject : EmbeddedRealmObject {
    var name: String = ""
    var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
}

class SummaryProjectObject : EmbeddedRealmObject {
    var name: String = ""
    var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var decimal: Float = 0f
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
}

class LanguageObject : EmbeddedRealmObject, GeneralizedEntityObject {
    override var name: String = ""
    override var totalSeconds: Float = 0f
    var percent: Float? = null
    var digital: String? = null
    var text: String? = null
    var hours: Int? = null
    var minutes: Int? = null
    var seconds: Int? = null
}

class EditorObject : EmbeddedRealmObject, GeneralizedEntityObject {
    override var name: String = ""
    override var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
}

class OperatingSystemObject : EmbeddedRealmObject, GeneralizedEntityObject {
    override var name: String = ""
    override var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
}

class DependencyObject : EmbeddedRealmObject {
    var name: String = ""
    var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
}

class MachineObject : EmbeddedRealmObject, GeneralizedEntityObject {
    override var name: String = ""
    override var totalSeconds: Float = 0f
    var machineNameId: String = ""
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
}

class BranchObject : EmbeddedRealmObject {
    var name: String = ""
    var totalSeconds: Float = 0f
    var percent: Float = 0f
    var digital: String = ""
    var text: String = ""
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
}

class RangeObject : EmbeddedRealmObject {
    var date: String = ""
    var text: String = ""
}

class CumulativeTotalObject : EmbeddedRealmObject {
    var seconds: Float = 0f
    var text: String = ""
    var digital: String = ""
}

class DailyAverageObject : EmbeddedRealmObject {
    var seconds: Float = 0f
    var text: String = ""
}
