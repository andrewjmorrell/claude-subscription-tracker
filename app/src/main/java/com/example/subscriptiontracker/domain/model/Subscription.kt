package com.example.subscriptiontracker.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/** Domain model for a recurring subscription. */
data class Subscription(
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val billingCycle: BillingCycle,
    val category: Category,
    val nextBillingDate: LocalDate,
    val notes: String = "",
    val trialEndDate: LocalDate? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    val monthlyCost: Double get() = billingCycle.toMonthlyAmount(amount)
    val yearlyCost: Double get() = billingCycle.toYearlyAmount(amount)
    val isInTrial: Boolean get() = trialEndDate != null && LocalDate.now().isBefore(trialEndDate)
    val daysUntilNextBilling: Long get() = ChronoUnit.DAYS.between(LocalDate.now(), nextBillingDate)
}
