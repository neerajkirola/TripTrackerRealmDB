package com.emt.tracker.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.emt.tracker.R
import com.emt.tracker.db.TripDao
import com.emt.tracker.notification.NotificationHandler
import com.emt.tracker.permission.PermissionUtils
import com.emt.tracker.service.TrackerService.Tracker.ACTION_STOP_SERVICE
import com.emt.tracker.service.TrackerService.Tracker.currentTripId
import com.emt.tracker.util.ToastUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class TrackerService : Service() {


    private val TAG = TrackerService::class.java.simpleName

    private val LOCATION_REQUEST_INTERVAL = 5000L
    private val LOCATION_REQUEST_INTERVAL_FASTEST = 1000L

    private val notifHandler by lazy { NotificationHandler() }

    object Tracker {
        val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        var currentTripId: String = System.currentTimeMillis().toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        startForeground(1, notifHandler.createNotificationChanel(this, this))
        setUpLocationListener()
        ToastUtils.showShortMessage(getString(R.string.trip_started), this)
        currentTripId = System.currentTimeMillis().toString()
        TripDao.createNewTrip(currentTripId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ACTION_STOP_SERVICE == intent?.getAction()) {
            Log.d(TAG, "called to cancel service");
            endTrip()
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().setInterval(LOCATION_REQUEST_INTERVAL)
            .setFastestInterval(LOCATION_REQUEST_INTERVAL)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (PermissionUtils.isAccessFineLocationGranted(this)) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        if (result.locations.isNotEmpty()) {
                            result.locations.first().let {
                                val lat = it.latitude
                                val long = it.longitude
                                val accuracy = it.accuracy
                                TripDao.insertTripData(currentTripId, lat, long, accuracy)
                                Log.v(
                                    TAG,
                                    "Latitude: ${it.latitude.toString()}, Longitude: ${it.longitude.toString()}"
                                )
                            }
                        }

                    }
                },
                Looper.myLooper()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endTrip()
        ToastUtils.showShortMessage(getString(R.string.trip_end), this)
    }

    private fun endTrip() {
        TripDao.endTrip(currentTripId)
    }
}