package com.example.subscriptiontracker.domain.model

/** How far in advance to send a billing reminder. */
enum class ReminderTiming(val label: String, val daysBefore: Int) {
    SAME_DAY("Same day", 0),
    ONE_DAY_BEFORE("1 day before", 1),
    THREE_DAYS_BEFORE("3 days before", 3)
}
