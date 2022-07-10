package com.emt.tracker.db

import com.emt.tracker.util.Utils
import io.realm.Realm
import io.realm.Sort

object TripDao {

    fun createNewTrip(tripId: String) {
        Realm.getDefaultInstance().executeTransactionAsync {
            var trip: Trip? = it.where(Trip::class.java).equalTo(COLUMN_TRIP_ID, tripId).findFirst()
            if (trip == null) {
                trip = it.createObject(Trip::class.java, tripId)
                trip.startTime = Utils.getCurrentTimeStamp()
            }
        }
    }

    fun insertTripData(tripId: String, latitude: Double, longitude: Double, accuracy: Float) {
        Realm.getDefaultInstance().executeTransactionAsync {
            var trip: Trip? = it.where(Trip::class.java).equalTo(COLUMN_TRIP_ID, tripId).findFirst()
            if (trip == null) {
                trip = it.createObject(Trip::class.java, tripId)
                trip.startTime = Utils.getCurrentTimeStamp()
                addLocation(trip, latitude, longitude, accuracy)
            } else {
                addLocation(trip, latitude, longitude, accuracy)
            }
        }
    }

    private fun addLocation(trip: Trip, latitude: Double, longitude: Double, accuracy: Float) {
        val location = TripData()
        location.latitude = latitude
        location.longitude = longitude
        location.accuracy = accuracy
        location.timeStamp = Utils.getCurrentTimeStamp()
        trip.locations?.add(location)
    }

    fun endTrip(tripId: String) {
        Realm.getDefaultInstance().executeTransactionAsync {
            var trip: Trip? = it.where(Trip::class.java).equalTo(COLUMN_TRIP_ID, tripId).findFirst()
            if (trip != null) {
                trip.endTime = Utils.getCurrentTimeStamp()
            }
        }
    }

    fun getAllTrips(success: (MutableList<Trip>?) -> Unit, error: (String) -> Unit) {
        val realm = Realm.getDefaultInstance()
        var result: MutableList<Trip>? = null
        realm.executeTransactionAsync(Realm.Transaction {
            result = it.copyFromRealm(
                it.where(Trip::class.java).sort(COLUMN_START_TIME, Sort.DESCENDING).findAll()
            )
        },
            Realm.Transaction.OnSuccess {

                success(result)
            },
            Realm.Transaction.OnError {
                error("Error in finding trips")
            })
    }

}