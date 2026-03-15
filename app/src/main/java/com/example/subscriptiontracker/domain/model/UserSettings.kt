package com.example.subscriptiontracker.domain.model

/** User preferences. */
data class UserSettings(
    val currencySymbol: String = "$",
    val reminderTiming: ReminderTiming = ReminderTiming.ONE_DAY_BEFORE,
    val remindersEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)
