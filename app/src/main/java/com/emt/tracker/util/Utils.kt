package com.emt.tracker.util

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.emt.tracker.TrackerApplication
import com.emt.tracker.db.Trip
import java.util.*


object Utils {

    fun getCurrentTimeStamp(): String {
        return DateFormater.getFormattedTimeStamp(System.currentTimeMillis())
    }

    fun getEndTime(trip: Trip): String {
        return "End at: ${
            if (trip.endTime == null) getLastTripTimeStamp(trip) else
                DateFormater.getFormattedDate(
                    trip.endTime!!
                )
        }"
    }

    fun getLastTripTimeStamp(trip: Trip): String {
        if (trip.locations==null || trip.locations?.isEmpty() == true) return ""
        val time = trip.locations?.last()?.timeStamp
        return if (time != null) DateFormater.getFormattedDate(time) else ""
    }

    private fun getAddressFromLocation(lat: Double?, long: Double?): String? {
        var strAdd = ""
        if (lat == null || long == null) return "Address not found."
        val geocoder = Geocoder(TrackerApplication.App.mContext, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(lat, long, 1)
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w(" address", strReturnedAddress.toString())
            } else {
                Log.w("address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("address", "Canont get Address!")
        }
        return strAdd
    }

    fun getStartEndAddressTrip(trip: Trip): Pair<String?, String?> {
        if (trip.locations == null || trip.locations?.isEmpty() == true) return Pair("", "")
        val startLoc = trip.locations?.first()
        val endLoc = trip.locations?.last()
        val sAddress = getAddressFromLocation(startLoc?.latitude, startLoc?.longitude)
        val eAddress = getAddressFromLocation(endLoc?.latitude, endLoc?.longitude)
        return Pair(sAddress, eAddress)

    }
}