package com.emt.tracker.db

import io.realm.RealmObject

open class TripData : RealmObject() {

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var timeStamp: String = ""
    var accuracy = 0F
}