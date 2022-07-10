package com.emt.tracker.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.emt.tracker.R
import com.emt.tracker.service.TrackerService
import com.emt.tracker.ui.TrackerActivity


class NotificationHandler {
    val NOTIFICATION_CHANNEL_ID = "tracker_channel_id"
    val channelName = "tracker"

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChanel(context: Context, service: Service): Notification {
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        ).apply {
            lightColor = Color.BLUE
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }

        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        val stopSelf = createStopButtonIntent(context, service)

        val resultIntent = Intent(context, TrackerActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(resultIntent)
        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        return notificationBuilder.setOngoing(true)
            .setContentIntent(resultPendingIntent)
            .setContentTitle(context.getString(R.string.app_name))
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(R.mipmap.ic_launcher, "Cancel", stopSelf)
            .build()
    }

    private fun createStopButtonIntent(context: Context, myService: Service): PendingIntent? {
        val stopSelf = Intent(context, TrackerService::class.java)
        stopSelf.action = TrackerService.Tracker.ACTION_STOP_SERVICE
        return PendingIntent.getService(
            myService, 0,
            stopSelf, PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}