package com.example.subscriptiontracker

import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.SortOption
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.util.categorySpending
import com.example.subscriptiontracker.util.filterSubscriptions
import com.example.subscriptiontracker.util.mostExpensive
import com.example.subscriptiontracker.util.sortSubscriptions
import com.example.subscriptiontracker.util.totalMonthlyCost
import com.example.subscriptiontracker.util.totalYearlyCost
import com.example.subscriptiontracker.util.upcomingBills
import com.example.subscriptiontracker.util.validateSubscription
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for subscription helper functions in [com.example.subscriptiontracker.util].
 */
class SubscriptionHelpersTest {

    private fun makeSub(
        name: String = "Test",
        amount: Double = 10.0,
        cycle: BillingCycle = BillingCycle.MONTHLY,
        category: Category = Category.STREAMING,
        nextBilling: LocalDate = LocalDate.now().plusDays(5),
        isActive: Boolean = true
    ) = Subscription(
        name = name,
        amount = amount,
        billingCycle = cycle,
        category = category,
        nextBillingDate = nextBilling,
        isActive = isActive
    )

    @Test
    fun `totalMonthlyCost sums only active subscriptions`() {
        val subs = listOf(
            makeSub(amount = 10.0, isActive = true),
            makeSub(amount = 20.0, isActive = true),
            makeSub(amount = 100.0, isActive = false)
        )
        assertEquals(30.0, totalMonthlyCost(subs), 0.001)
    }

    @Test
    fun `totalMonthlyCost returns 0 for empty list`() {
        assertEquals(0.0, totalMonthlyCost(emptyList()), 0.001)
    }

    @Test
    fun `totalYearlyCost sums yearly costs of active subscriptions`() {
        val subs = listOf(makeSub(amount = 10.0))
        assertEquals(120.0, totalYearlyCost(subs), 0.001)
    }

    @Test
    fun `categorySpending groups by category and sorts by monthly total`() {
        val subs = listOf(
            makeSub(amount = 5.0, category = Category.MUSIC),
            makeSub(amount = 15.0, category = Category.STREAMING),
            makeSub(amount = 10.0, category = Category.STREAMING)
        )
        val result = categorySpending(subs)
        assertEquals(2, result.size)
        assertEquals(Category.STREAMING, result[0].category)
        assertEquals(25.0, result[0].monthlyTotal, 0.001)
        assertEquals(2, result[0].count)
        assertEquals(Category.MUSIC, result[1].category)
    }

    @Test
    fun `upcomingBills returns only bills within specified days`() {
        val subs = listOf(
            makeSub(nextBilling = LocalDate.now()),
            makeSub(nextBilling = LocalDate.now().plusDays(10)),
            makeSub(nextBilling = LocalDate.now().plusDays(40))
        )
        val result = upcomingBills(subs, days = 30)
        assertEquals(2, result.size)
    }

    @Test
    fun `upcomingBills excludes past bills`() {
        val subs = listOf(
            makeSub(nextBilling = LocalDate.now().minusDays(1)),
            makeSub(nextBilling = LocalDate.now().plusDays(1))
        )
        assertEquals(1, upcomingBills(subs, days = 30).size)
    }

    @Test
    fun `upcomingBills excludes inactive subscriptions`() {
        val subs = listOf(
            makeSub(nextBilling = LocalDate.now().plusDays(1), isActive = false)
        )
        assertEquals(0, upcomingBills(subs, days = 30).size)
    }

    @Test
    fun `sortSubscriptions by NAME sorts alphabetically`() {
        val subs = listOf(
            makeSub(name = "Zebra"),
            makeSub(name = "Apple"),
            makeSub(name = "Mango")
        )
        val sorted = sortSubscriptions(subs, SortOption.NAME)
        assertEquals("Apple", sorted[0].name)
        assertEquals("Mango", sorted[1].name)
        assertEquals("Zebra", sorted[2].name)
    }

    @Test
    fun `sortSubscriptions by AMOUNT_ASC sorts ascending`() {
        val subs = listOf(
            makeSub(amount = 30.0),
            makeSub(amount = 10.0),
            makeSub(amount = 20.0)
        )
        val sorted = sortSubscriptions(subs, SortOption.AMOUNT_ASC)
        assertEquals(10.0, sorted[0].amount, 0.001)
        assertEquals(20.0, sorted[1].amount, 0.001)
        assertEquals(30.0, sorted[2].amount, 0.001)
    }

    @Test
    fun `sortSubscriptions by AMOUNT_DESC sorts descending`() {
        val subs = listOf(
            makeSub(amount = 10.0),
            makeSub(amount = 30.0)
        )
        val sorted = sortSubscriptions(subs, SortOption.AMOUNT_DESC)
        assertEquals(30.0, sorted[0].amount, 0.001)
    }

    @Test
    fun `sortSubscriptions by NEXT_BILLING sorts by date`() {
        val subs = listOf(
            makeSub(nextBilling = LocalDate.now().plusDays(10)),
            makeSub(nextBilling = LocalDate.now().plusDays(1))
        )
        val sorted = sortSubscriptions(subs, SortOption.NEXT_BILLING)
        assertTrue(sorted[0].nextBillingDate.isBefore(sorted[1].nextBillingDate))
    }

    @Test
    fun `filterSubscriptions by query matches name case-insensitively`() {
        val subs = listOf(
            makeSub(name = "Netflix"),
            makeSub(name = "Spotify"),
            makeSub(name = "Disney Plus")
        )
        val result = filterSubscriptions(subs, query = "net")
        assertEquals(1, result.size)
        assertEquals("Netflix", result[0].name)
    }

    @Test
    fun `filterSubscriptions by category returns only matching category`() {
        val subs = listOf(
            makeSub(category = Category.STREAMING),
            makeSub(category = Category.MUSIC),
            makeSub(category = Category.STREAMING)
        )
        val result = filterSubscriptions(subs, category = Category.STREAMING)
        assertEquals(2, result.size)
    }

    @Test
    fun `filterSubscriptions with blank query returns all`() {
        val subs = listOf(makeSub(), makeSub())
        assertEquals(2, filterSubscriptions(subs, query = "").size)
    }

    @Test
    fun `mostExpensive returns top N by monthly cost`() {
        val subs = (1..10).map { makeSub(amount = it.toDouble()) }
        val result = mostExpensive(subs, limit = 3)
        assertEquals(3, result.size)
        assertEquals(10.0, result[0].amount, 0.001)
    }

    @Test
    fun `mostExpensive excludes inactive`() {
        val subs = listOf(
            makeSub(amount = 100.0, isActive = false),
            makeSub(amount = 50.0, isActive = true)
        )
        val result = mostExpensive(subs, limit = 5)
        assertEquals(1, result.size)
        assertEquals(50.0, result[0].amount, 0.001)
    }

    @Test
    fun `validateSubscription returns errors for blank name`() {
        val errors = validateSubscription("", "10.0", LocalDate.now())
        assertTrue(errors.containsKey("name"))
    }

    @Test
    fun `validateSubscription returns errors for invalid amount`() {
        val errors = validateSubscription("Test", "abc", LocalDate.now())
        assertTrue(errors.containsKey("amount"))
    }

    @Test
    fun `validateSubscription returns errors for zero amount`() {
        val errors = validateSubscription("Test", "0", LocalDate.now())
        assertTrue(errors.containsKey("amount"))
    }

    @Test
    fun `validateSubscription returns errors for null date`() {
        val errors = validateSubscription("Test", "10.0", null)
        assertTrue(errors.containsKey("nextBillingDate"))
    }

    @Test
    fun `validateSubscription returns empty map for valid input`() {
        val errors = validateSubscription("Netflix", "15.99", LocalDate.now())
        assertTrue(errors.isEmpty())
    }
}
