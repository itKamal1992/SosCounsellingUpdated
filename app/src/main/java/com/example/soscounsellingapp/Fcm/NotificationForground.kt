package com.example.soscounsellingapp.Fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.soscounsellingapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class NotificationForground :FirebaseMessagingService()
{

    @SuppressLint("WrongConstant")
    override fun onMessageReceived(p0: RemoteMessage?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = "id_product"
            // The user-visible name of the channel.
            val name = "Sos Counselling"
            // The user-visible description of the channel.
            val description = "Notifications Sos"
            val importance = NotificationManager.IMPORTANCE_MAX
            val mChannel = NotificationChannel(id, name, importance)
            // Configure the notification channel.
            mChannel.description = description
            mChannel.enableLights(true)
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.lightColor = Color.RED
            notificationManager.createNotificationChannel(mChannel)


            if (p0!!.getNotification() != null) {
                val notification = NotificationCompat.Builder(this, "id_product")
                    .setContentTitle(p0.getNotification()!!.getTitle()!!.toUpperCase(Locale(p0.getNotification()!!.getTitle())))
                    .setContentText(p0.getNotification()!!.getBody()!!.capitalize())
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setChannelId(id)
                    .setNumber(1)
//                    .setColor(255)
                    .setWhen(System.currentTimeMillis())
                    .build()
                val manager = NotificationManagerCompat.from(applicationContext)
                manager.notify(1, notification)
                println("Hello")
            }

        } else {
            if (p0!!.getNotification() != null) {
                val notification = NotificationCompat.Builder(this, "Test_Coding")
                    .setContentTitle(p0!!.getNotification()!!.getTitle())
                    .setContentText(p0.getNotification()!!.getBody())
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build()
                val manager = NotificationManagerCompat.from(applicationContext)
                manager.notify(1, notification)

            }

        }
    }


}