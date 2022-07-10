package com.emt.tracker.db

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Trip : RealmObject() {
    @PrimaryKey
    var tripId: String = ""
    var startTime: String? = null
    var endTime: String? = null
    var locations: RealmList<TripData>? = null
}