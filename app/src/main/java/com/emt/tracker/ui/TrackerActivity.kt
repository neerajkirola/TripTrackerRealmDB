package com.emt.tracker.ui

import android.app.ActivityManager
import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emt.tracker.R
import com.emt.tracker.databinding.ActivityTrackerBinding
import com.emt.tracker.db.RealmController
import com.emt.tracker.permission.PermissionUtils
import com.emt.tracker.service.TrackerService
import com.emt.tracker.ui.triplist.TripListActivity


class TrackerActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binder: ActivityTrackerBinding

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    var notification: Notification? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RealmController.initReamDb()
        binder = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(binder.root)

        binder.tvStartTrip.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        updateButtontext()
    }

    private fun updateButtontext() {

        binder
            .tvStartTrip.text = if (isOnTrip()) {
            getString(R.string.stop_trip)
        } else getString(R.string.start_trip)
    }

    private fun startTracker() {
        val intent = Intent().apply {
            setClass(this@TrackerActivity, TrackerService::class.java)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else startService(intent)
        updateButtontext()
    }

    private fun stopTracker() {
        stopService(Intent().apply {
            setClass(this@TrackerActivity, TrackerService::class.java)
        })
        updateButtontext()
    }

    private fun isOnTrip(): Boolean {
        return isMyServiceRunning()
    }

    private fun trackerButtonClicked() {
        if (isOnTrip()) {
            stopTracker()
        } else if (checkPermission()) {
            startTracker()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvStartTrip -> trackerButtonClicked()
            R.id.tvTrips -> gotoTrips()
        }
    }

    private fun gotoTrips() {
        startActivity(Intent(this, TripListActivity::class.java))
    }

    private fun isMyServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (TrackerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            startTracker()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        return when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        true
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                        false
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                false
            }
        }
    }
}