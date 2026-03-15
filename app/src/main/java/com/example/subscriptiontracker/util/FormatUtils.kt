package com.example.subscriptiontracker.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun formatCurrency(amount: Double, symbol: String): String = "$symbol${"%.2f".format(amount)}"

fun formatDate(date: LocalDate): String = date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))

fun formatDaysUntil(date: LocalDate): String {
    val d = ChronoUnit.DAYS.between(LocalDate.now(), date)
    return when {
        d < 0 -> "${-d} days overdue"
        d == 0L -> "Due today"
        d == 1L -> "Due tomorrow"
        d <= 7 -> "In $d days"
        else -> formatDate(date)
    }
}
