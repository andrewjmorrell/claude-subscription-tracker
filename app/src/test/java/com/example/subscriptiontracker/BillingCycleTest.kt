package com.example.subscriptiontracker

import com.example.subscriptiontracker.domain.model.BillingCycle
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [BillingCycle] monthly and yearly amount conversion methods.
 */
class BillingCycleTest {

    @Test
    fun `WEEKLY toMonthlyAmount converts correctly`() {
        val result = BillingCycle.WEEKLY.toMonthlyAmount(10.0)
        assertEquals(10.0 * 52.0 / 12.0, result, 0.01)
    }

    @Test
    fun `MONTHLY toMonthlyAmount returns same amount`() {
        assertEquals(15.99, BillingCycle.MONTHLY.toMonthlyAmount(15.99), 0.001)
    }

    @Test
    fun `QUARTERLY toMonthlyAmount divides by 3`() {
        assertEquals(10.0, BillingCycle.QUARTERLY.toMonthlyAmount(30.0), 0.001)
    }

    @Test
    fun `SEMI_ANNUAL toMonthlyAmount divides by 6`() {
        assertEquals(5.0, BillingCycle.SEMI_ANNUAL.toMonthlyAmount(30.0), 0.001)
    }

    @Test
    fun `ANNUAL toMonthlyAmount divides by 12`() {
        assertEquals(10.0, BillingCycle.ANNUAL.toMonthlyAmount(120.0), 0.001)
    }

    @Test
    fun `WEEKLY toYearlyAmount multiplies by 52`() {
        assertEquals(520.0, BillingCycle.WEEKLY.toYearlyAmount(10.0), 0.001)
    }

    @Test
    fun `MONTHLY toYearlyAmount multiplies by 12`() {
        assertEquals(120.0, BillingCycle.MONTHLY.toYearlyAmount(10.0), 0.001)
    }

    @Test
    fun `QUARTERLY toYearlyAmount multiplies by 4`() {
        assertEquals(40.0, BillingCycle.QUARTERLY.toYearlyAmount(10.0), 0.001)
    }

    @Test
    fun `SEMI_ANNUAL toYearlyAmount multiplies by 2`() {
        assertEquals(20.0, BillingCycle.SEMI_ANNUAL.toYearlyAmount(10.0), 0.001)
    }

    @Test
    fun `ANNUAL toYearlyAmount returns same amount`() {
        assertEquals(120.0, BillingCycle.ANNUAL.toYearlyAmount(120.0), 0.001)
    }

    @Test
    fun `all billing cycles have correct labels`() {
        assertEquals("Weekly", BillingCycle.WEEKLY.label)
        assertEquals("Monthly", BillingCycle.MONTHLY.label)
        assertEquals("Quarterly", BillingCycle.QUARTERLY.label)
        assertEquals("Semi-Annual", BillingCycle.SEMI_ANNUAL.label)
        assertEquals("Annual", BillingCycle.ANNUAL.label)
    }
}
