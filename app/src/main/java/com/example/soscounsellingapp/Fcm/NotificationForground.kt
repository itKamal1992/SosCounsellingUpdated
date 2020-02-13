package com.example.soscounsellingapp.Fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.remote.ApiClientPhp
import com.example.soscounsellingapp.remote.PhpApiInterface
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.net.URL
import java.util.*

class NotificationForground :FirebaseMessagingService()
{


    @SuppressLint("WrongConstant")
    override fun onMessageReceived(p0: RemoteMessage?) {
        println("Executed notification  .........22")

//check()

        if (p0?.data!!["link"].toString() == "-") {



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
                mChannel.lightColor = Color.WHITE
                notificationManager.createNotificationChannel(mChannel)


                if (p0.data != null )
                {
                    val notification = NotificationCompat.Builder(this, "id_product")
                        .setContentTitle(p0.data["title"]!!.toUpperCase(Locale(p0.data["title"]!!)))
                        .setContentText(p0.data["body"]!!.capitalize())
                        .setStyle(NotificationCompat.BigTextStyle())
                        .setSmallIcon(R.drawable.sos_paw)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setChannelId(id)
                        .setLights(Color.MAGENTA, 500, 500)
                        .setNumber(1)
                        .setColor(Color.YELLOW)
                        .setWhen(System.currentTimeMillis())
                        .build()
                    val manager = NotificationManagerCompat.from(applicationContext)
                    manager.notify(1, notification)
                    println("blank")
                }

            } else {
                if (p0.data != null ) {

                    p0.data["link"]!!
                    val notification = NotificationCompat.Builder(this, "Test_Coding")
                        .setContentTitle(p0.data["title"]!!)
                        .setContentText(p0.data["body"]!!)
                        .setStyle(NotificationCompat.BigTextStyle())
                        .setSmallIcon(R.drawable.sos_paw)
                        .setLights(Color.MAGENTA, 500, 500)
                        .build()
                    val manager = NotificationManagerCompat.from(applicationContext)
                    manager.notify(1, notification)

                    println("blank")
                }
            }
        }else
        {
            var inputStream = URL(p0.data["link"]!!.toString()).openStream()
            val messageImage = BitmapFactory.decodeStream(inputStream)




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




                val notification = NotificationCompat.Builder(this, "id_product")
                    .setContentTitle(p0.data["title"]!!.toUpperCase(Locale(p0.data["title"]!!)))
                    .setContentText(p0.data["body"]!!.capitalize())
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setSmallIcon(R.drawable.sos_paw)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setChannelId(id)
                    .setNumber(1)
                    .setLargeIcon(messageImage)
                    .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(messageImage)
                        .bigLargeIcon(null))
                    .setColor(Color.YELLOW)
                    .setLights(Color.MAGENTA, 500, 500)
                    .setWhen(System.currentTimeMillis())
                    .build()
                val manager = NotificationManagerCompat.from(applicationContext)
                manager.notify(1, notification)
                println("not blank")


        } else {


            var inputStream  = URL(p0.data["link"]!!.toString()).openStream()
                val messageImage = BitmapFactory.decodeStream(inputStream)
                val notification = NotificationCompat.Builder(this, "Test_Coding")
                    .setContentTitle(p0.data["title"]!!)
                    .setContentText(p0.data["body"]!!)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setLargeIcon(messageImage)
                    .setStyle(NotificationCompat.BigPictureStyle()
                        .bigPicture(messageImage)
                        .bigLargeIcon(null))
                    .setSmallIcon(R.drawable.sos_paw)
                    .setLights(Color.MAGENTA, 500, 500)
                    .build()
                val manager = NotificationManagerCompat.from(applicationContext)
                manager.notify(1, notification)
                println("Not blank")


        }
    }

    }

//    private fun check() {
//        try {
//
//            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
//                PhpApiInterface::class.java
//            )
//            /*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*/
//
//            var call3: Call<APIResponse> = phpApiInterface.RegisterUser("Register","B","8888888888","B","B","B","B","B","B","B","8888888888")
//
//            call3.enqueue(object : Callback<APIResponse> {
//                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//
//                }
//
//                override fun onResponse(
//                    call: Call<APIResponse>,
//                    response: Response<APIResponse>
//                ) {
//                    try {
//                        val result: APIResponse? = response.body()
//                        println("result  " + result)
//
//                    } catch (ex: Exception) {
//                        ex.printStackTrace()
//                    }
//                }
//            })
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }
}