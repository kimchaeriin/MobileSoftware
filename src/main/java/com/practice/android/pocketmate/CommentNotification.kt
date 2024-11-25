package com.practice.android.pocketmate

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.practice.android.pocketmate.util.FBAuth

class CommentNotification(private val context: Context) {
    private var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            createNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID, CHANNERL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            enableVibration(true)
            description = "get comment"
        }

        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun deliverNotification() {
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_send_24)
            .setContentTitle("댓글이 도착했어요!")
            .setContentText("회원님의 게시글에 댓글이 달렸습니다.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "comment_CHANNEL"
        const val CHANNERL_NAME = "Comment CHANNEL"
        const val NOTIFICATION_ID = 0
    }
}