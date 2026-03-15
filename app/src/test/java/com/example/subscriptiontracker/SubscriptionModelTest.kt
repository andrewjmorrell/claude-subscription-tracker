package com.example.subscriptiontracker

import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.Subscription
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for the [Subscription] domain model computed properties.
 */
class SubscriptionModelTest {

    private fun makeSub(
        amount: Double = 10.0,
        cycle: BillingCycle = BillingCycle.MONTHLY,
        nextBilling: LocalDate = LocalDate.now().plusDays(5),
        trialEndDate: LocalDate? = null
    ) = Subscription(
        name = "Test",
        amount = amount,
        billingCycle = cycle,
        category = Category.STREAMING,
        nextBillingDate = nextBilling,
        trialEndDate = trialEndDate
    )

    @Test
    fun `monthlyCost delegates to BillingCycle toMonthlyAmount`() {
        val sub = makeSub(amount = 120.0, cycle = BillingCycle.ANNUAL)
        assertEquals(10.0, sub.monthlyCost, 0.001)
    }

    @Test
    fun `yearlyCost delegates to BillingCycle toYearlyAmount`() {
        val sub = makeSub(amount = 10.0, cycle = BillingCycle.MONTHLY)
        assertEquals(120.0, sub.yearlyCost, 0.001)
    }

    @Test
    fun `isInTrial returns true when trial end date is in the future`() {
        val sub = makeSub(trialEndDate = LocalDate.now().plusDays(7))
        assertTrue(sub.isInTrial)
    }

    @Test
    fun `isInTrial returns false when trial end date is in the past`() {
        val sub = makeSub(trialEndDate = LocalDate.now().minusDays(1))
        assertFalse(sub.isInTrial)
    }

    @Test
    fun `isInTrial returns false when trial end date is null`() {
        val sub = makeSub(trialEndDate = null)
        assertFalse(sub.isInTrial)
    }

    @Test
    fun `daysUntilNextBilling calculates correctly`() {
        val sub = makeSub(nextBilling = LocalDate.now().plusDays(10))
        assertEquals(10, sub.daysUntilNextBilling)
    }

    @Test
    fun `daysUntilNextBilling returns negative for past dates`() {
        val sub = makeSub(nextBilling = LocalDate.now().minusDays(3))
        assertEquals(-3, sub.daysUntilNextBilling)
    }

    @Test
    fun `daysUntilNextBilling returns 0 for today`() {
        val sub = makeSub(nextBilling = LocalDate.now())
        assertEquals(0, sub.daysUntilNextBilling)
    }
}
