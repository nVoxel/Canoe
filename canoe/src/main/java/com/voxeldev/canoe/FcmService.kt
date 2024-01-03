package com.voxeldev.canoe

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

/**
 * @author nvoxel
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
internal class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            createNotificationChannel()

            with(resources) {
                val builder = NotificationCompat.Builder(
                    applicationContext,
                    getString(R.string.fcm_notification_channel_id),
                )
                    .setContentTitle(message.notification?.title ?: getString(R.string.fcm_notification_default_title))
                    .setContentText(message.notification?.body ?: getString(R.string.fcm_notification_default_text))
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setPriority(
                        if (message.priority == RemoteMessage.PRIORITY_HIGH) {
                            NotificationCompat.PRIORITY_MAX
                        } else {
                            NotificationCompat.PRIORITY_DEFAULT
                        },
                    )

                getNotificationManager().notify(getNotificationId(), builder.build())
            }
        }
    }

    private fun getNotificationManager(): NotificationManager = applicationContext.getSystemService(
        NotificationManager::class.java,
    )

    private fun getNotificationId(): Int = Random.nextInt()

    private fun createNotificationChannel() {
        with(resources) {
            val notificationChannel = NotificationChannel(
                getString(R.string.fcm_notification_channel_id),
                getString(R.string.fcm_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).also { it.description = getString(R.string.fcm_notification_channel_description) }

            getNotificationManager().createNotificationChannel(notificationChannel)
        }
    }
}
