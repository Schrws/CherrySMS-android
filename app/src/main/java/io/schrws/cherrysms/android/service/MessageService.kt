package io.schrws.cherrysms.android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.provider.Telephony
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.schrws.cherrysms.android.di.Api
import org.koin.android.ext.android.inject
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.schrws.cherrysms.android.di.Constants.Companion.CHANNEL_ID
import io.schrws.cherrysms.android.di.SharedPreferenceStorage

class MessageService : Service() {
    private val shared: SharedPreferenceStorage by inject()
    private val api: Api by inject()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, "cherry", NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = "Cherry SMS"
            NotificationManagerCompat.from(this).createNotificationChannel(mChannel)
        }

        startForeground(1847, NotificationCompat.Builder(this, CHANNEL_ID).build())
        registerReceiver(smsReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private val smsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                    for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
                        val body = JsonObject()
                        body.addProperty("id", shared.telegramID)
                        body.addProperty("from", smsMessage.displayOriginatingAddress)
                        body.addProperty("message", smsMessage.messageBody)

                        api.postMessage(body)
                            .retry()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({}, {})
                    }
                }
            }
        }

    }
}