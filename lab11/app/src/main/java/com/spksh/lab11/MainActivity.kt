package com.spksh.lab11

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            requestMultiplePermissions()
            val context: Context = this@MainActivity
            var newnotchan: NotificationChannel? = null
            newnotchan = NotificationChannel(
                "mychannel1",
                "mychannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            newnotchan.setSound(
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName +
                        "/" + R.raw.sound),
                audioAttributes
            )
            val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as
                        NotificationManager
            notificationManager.createNotificationChannel(newnotchan)
            var notification: Notification? = null
            notification = Notification.Builder(context, "mychannel1")
                .setContentTitle("Hi from Alex")
                .setContentText("Hey, listen!")
                .setTicker("new notification!")
                .setChannelId("mychannel1")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setOngoing(true)
                .build()
            notificationManager.notify(0, notification)

        }
    }
    fun requestMultiplePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(
                POST_NOTIFICATIONS
            ),
            PERMISSION_REQUEST_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        vararg permissions: String,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return
            } else finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}