package com.example.minorprojecthealthcare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val medname = intent!!.getStringExtra("medname")
        val description = intent!!.getStringExtra("description")
        val nm = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            nm.createNotificationChannel(
                NotificationChannel(
                    "first",
                    "alarmreminder",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val v = longArrayOf(500,1000)
        val notification = NotificationCompat.Builder(context,"first")
            .setContentTitle("Time to take"+medname)
            .setContentText(description)
            .setAutoCancel(false)
            .setVibrate(v)
            .setSound(uri)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        nm.notify(System.currentTimeMillis().toInt(),notification)

    }

}
