package com.example.internshipmanagement.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FCMHelper : FirebaseMessagingService() {

    private var gcmId = ""

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        gcmId = token
        Log.d("###", "NewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("###", "New message")
        if(message != null) {
            handleRemoteMessage(message)
        }
    }

    private fun handleRemoteMessage(message: RemoteMessage) {
        val sharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")
        val param: Map<String, String> = message.data
        val data = JSONObject(param)
        if(data.toString().isNotEmpty()) {
            val id = data.get("id").toString().toInt()
            val toId = data.get("toId").toString()
            val content = (data.get("message")).toString()

            if(userId == toId) {
                // Báo cho dash board cập nhật badge notification
                val pushIntent = Intent(FCM_PUSH)
                sendBroadcast(pushIntent)

                showNotification(id, content)
            }


        }

    }

    private fun showNotification(id: Int, message: String) {
        val miniIcon = R.drawable.app_logo
        val desIntent = Intent(this, MainActivity::class.java)
        val desPendingIntent = PendingIntent.getActivity(this.applicationContext, id, desIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(miniIcon)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(desPendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(id, notificationBuilder.build())
    }

}