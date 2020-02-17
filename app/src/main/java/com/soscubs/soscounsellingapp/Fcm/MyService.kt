package com.soscubs.soscounsellingapp.Fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.soscubs.soscounsellingapp.R
import com.soscubs.soscounsellingapp.activity.SplashScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.InputStream
import java.net.URL
import java.util.*

class MyService : FirebaseMessagingService() {
    @SuppressLint("WrongConstant")
    override fun onMessageReceived(p0: RemoteMessage?) {
        // TODO(developer): Handle FCM messages here.
        var inputStream: InputStream
        inputStream = URL("http://dmimsdu.in/web/api_cubs/viewpager_image/common_img.jpg").openStream()
        val Icon = BitmapFactory.decodeStream(inputStream)

        println("Executed notification  .........11")
        if (p0!!.getNotification() != null) {
            val title_text = p0!!.getNotification()!!.getTitle()
            val body_text = p0.getNotification()!!.getBody()

            var title=title_text!!.toUpperCase()
            var body = body_text!!.capitalize()


            val intent = Intent(applicationContext, SplashScreen::class.java)

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                100,
                intent, PendingIntent.FLAG_CANCEL_CURRENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   println("Executed o")
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val id = "id_product"
                // The user-visible name of the channel.
                val name = "Sos Counselling"
                // The user-visible description of the channel.
                val description = "Notifications regarding Sos"
                val importance = NotificationManager.IMPORTANCE_MAX
                val mChannel = NotificationChannel(id, name, importance)


                // Configure the notification channel.



                mChannel.description = description
                mChannel.enableLights(true)
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.lightColor = Color.RED
                notificationManager.createNotificationChannel(mChannel)



                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)
                val mbuilder = NotificationCompat.Builder(applicationContext, "id_product")
                    .setSmallIcon(R.drawable.sos_paw)
                    .setContentTitle(p0.getNotification()!!.getTitle()!!.toUpperCase(Locale(p0.getNotification()!!.getTitle())))
                    .setContentText(body_text!!.capitalize())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(Icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                    .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(Icon)
                )
                    .setChannelId(id)
                    .setNumber(1)
                    .setColor(255)
                    .setWhen(System.currentTimeMillis())
                val mNotifiactionMgr = NotificationManagerCompat.from(applicationContext)
                mNotifiactionMgr.notify(1, mbuilder.build())

            }
            else
            {
                println("Executed not o here")
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_background)
                val mbuilder = NotificationCompat.Builder(applicationContext, "id_product")
                    .setSmallIcon(R.drawable.sos_paw)
                    .setContentTitle(p0.getNotification()!!.getTitle()!!.toUpperCase(Locale(p0.getNotification()!!.getTitle())))
                    .setContentText(body_text.capitalize())
          .setStyle(NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line..."))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(Icon)
                    .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(Icon)
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                val mNotifiactionMgr = NotificationManagerCompat.from(applicationContext)
                mNotifiactionMgr.notify(1, mbuilder.build())



            }

        }



        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + p0!!.getFrom());

        // Check if message contains a data payload.
        /*      if (p0!!.data.isNotEmpty()) {
      //            Log.d(TAG, "Message data payload: " + p0.getData());

                  if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (p0!!.notification != null) {

            Log.d(TAG, "Message Notification Body: " + p0.notification!!.body);

            println("aaaa  "+p0.notification!!.body)

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        super.onMessageReceived(p0)*/
    }

}