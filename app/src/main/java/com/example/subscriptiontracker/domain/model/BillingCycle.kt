package com.example.subscriptiontracker.domain.model

/** Recurring billing frequency for a subscription. */
enum class BillingCycle(val label: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    SEMI_ANNUAL("Semi-Annual"),
    ANNUAL("Annual");

    fun toMonthlyAmount(amount: Double): Double = when (this) {
        WEEKLY -> amount * 52.0 / 12.0
        MONTHLY -> amount
        QUARTERLY -> amount / 3.0
        SEMI_ANNUAL -> amount / 6.0
        ANNUAL -> amount / 12.0
    }

    fun toYearlyAmount(amount: Double): Double = when (this) {
        WEEKLY -> amount * 52.0
        MONTHLY -> amount * 12.0
        QUARTERLY -> amount * 4.0
        SEMI_ANNUAL -> amount * 2.0
        ANNUAL -> amount
    }
}
