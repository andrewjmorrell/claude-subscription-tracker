package com.example.subscriptiontracker.util

import com.example.subscriptiontracker.domain.model.CategorySpending
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.SortOption
import com.example.subscriptiontracker.domain.model.Subscription
import java.time.LocalDate

fun totalMonthlyCost(subs: List<Subscription>): Double =
    subs.filter { it.isActive }.sumOf { it.monthlyCost }

fun totalYearlyCost(subs: List<Subscription>): Double =
    subs.filter { it.isActive }.sumOf { it.yearlyCost }

fun categorySpending(subs: List<Subscription>): List<CategorySpending> =
    subs.filter { it.isActive }
        .groupBy { it.category }
        .map { (cat, list) -> CategorySpending(cat, list.sumOf { it.monthlyCost }, list.sumOf { it.yearlyCost }, list.size) }
        .sortedByDescending { it.monthlyTotal }

fun upcomingBills(subs: List<Subscription>, days: Int = 30): List<Subscription> {
    val today = LocalDate.now()
    val end = today.plusDays(days.toLong())
    return subs.filter { it.isActive && !it.nextBillingDate.isBefore(today) && !it.nextBillingDate.isAfter(end) }
        .sortedBy { it.nextBillingDate }
}

fun sortSubscriptions(subs: List<Subscription>, opt: SortOption): List<Subscription> = when (opt) {
    SortOption.NAME -> subs.sortedBy { it.name.lowercase() }
    SortOption.AMOUNT_ASC -> subs.sortedBy { it.amount }
    SortOption.AMOUNT_DESC -> subs.sortedByDescending { it.amount }
    SortOption.NEXT_BILLING -> subs.sortedBy { it.nextBillingDate }
}

fun filterSubscriptions(subs: List<Subscription>, query: String = "", category: Category? = null): List<Subscription> {
    var r = subs
    if (query.isNotBlank()) r = r.filter { it.name.contains(query, ignoreCase = true) }
    if (category != null) r = r.filter { it.category == category }
    return r
}

fun mostExpensive(subs: List<Subscription>, limit: Int = 5): List<Subscription> =
    subs.filter { it.isActive }.sortedByDescending { it.monthlyCost }.take(limit)

fun validateSubscription(name: String, amount: String, date: LocalDate?): Map<String, String> {
    val errors = mutableMapOf<String, String>()
    if (name.isBlank()) errors["name"] = "Name is required"
    val v = amount.toDoubleOrNull()
    if (v == null || v <= 0) errors["amount"] = "Enter a valid amount greater than 0"
    if (date == null) errors["nextBillingDate"] = "Next billing date is required"
    return errors
}
