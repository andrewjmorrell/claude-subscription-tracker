package com.example.subscriptiontracker.worker

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.subscriptiontracker.MainActivity
import com.example.subscriptiontracker.R
import com.example.subscriptiontracker.domain.repository.SettingsRepository
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.formatCurrency
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

/**
 * WorkManager worker that checks for upcoming subscription billing dates
 * and sends notifications to the user based on their reminder timing preferences.
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val subscriptionRepository: SubscriptionRepository,
    private val settingsRepository: SettingsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val settings = settingsRepository.getSettings().first()
        if (!settings.remindersEnabled) return Result.success()

        val hasPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return Result.success()

        val today = LocalDate.now()
        val targetDate = today.plusDays(settings.reminderTiming.daysBefore.toLong())
        val subscriptions = subscriptionRepository.getActive().first()

        val dueSubs = subscriptions.filter { it.nextBillingDate == targetDate }

        dueSubs.forEachIndexed { index, sub ->
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                sub.id.toInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val daysText = when (settings.reminderTiming.daysBefore) {
                0 -> "today"
                1 -> "tomorrow"
                else -> "in ${settings.reminderTiming.daysBefore} days"
            }

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("${sub.name} billing $daysText")
                .setContentText(
                    "${formatCurrency(sub.amount, settings.currencySymbol)} " +
                        "(${sub.billingCycle.label})"
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_BASE_ID + index, notification)
        }

        return Result.success()
    }

    companion object {
        const val CHANNEL_ID = "billing_reminders"
        const val NOTIFICATION_BASE_ID = 1000
    }
}
