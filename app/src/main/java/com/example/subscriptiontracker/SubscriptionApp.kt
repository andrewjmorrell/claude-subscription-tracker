package com.example.subscriptiontracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.subscriptiontracker.worker.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SubscriptionApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel("billing_reminders", "Billing Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            .apply { description = "Reminders for upcoming billing dates" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        ReminderScheduler.schedule(this)
    }
}
