package com.example.subscriptiontracker.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Utility object that schedules the daily [ReminderWorker] using WorkManager.
 * The worker runs once every 24 hours and checks for upcoming billing dates.
 */
object ReminderScheduler {

    private const val WORK_NAME = "billing_reminder_check"

    /**
     * Schedules a daily periodic work request for billing reminders.
     * Uses [ExistingPeriodicWorkPolicy.KEEP] to avoid re-scheduling if already queued.
     */
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
