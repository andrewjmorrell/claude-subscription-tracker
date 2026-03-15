package com.example.subscriptiontracker.domain.model

/** Aggregated spending for one category. */
data class CategorySpending(
    val category: Category,
    val monthlyTotal: Double,
    val yearlyTotal: Double,
    val count: Int
)
