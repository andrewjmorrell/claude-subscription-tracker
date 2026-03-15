package com.example.subscriptiontracker

import com.example.subscriptiontracker.util.formatCurrency
import com.example.subscriptiontracker.util.formatDate
import com.example.subscriptiontracker.util.formatDaysUntil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for formatting utility functions in [com.example.subscriptiontracker.util].
 */
class FormatUtilsTest {

    @Test
    fun `formatCurrency formats with dollar sign`() {
        assertEquals("$10.00", formatCurrency(10.0, "$"))
    }

    @Test
    fun `formatCurrency formats with euro sign`() {
        assertEquals("€15.99", formatCurrency(15.99, "€"))
    }

    @Test
    fun `formatCurrency formats zero`() {
        assertEquals("$0.00", formatCurrency(0.0, "$"))
    }

    @Test
    fun `formatCurrency rounds to two decimal places`() {
        assertEquals("$10.50", formatCurrency(10.499999, "$"))
    }

    @Test
    fun `formatDate formats correctly`() {
        val date = LocalDate.of(2024, 3, 15)
        assertEquals("Mar 15, 2024", formatDate(date))
    }

    @Test
    fun `formatDaysUntil returns Due today for today`() {
        assertEquals("Due today", formatDaysUntil(LocalDate.now()))
    }

    @Test
    fun `formatDaysUntil returns Due tomorrow for tomorrow`() {
        assertEquals("Due tomorrow", formatDaysUntil(LocalDate.now().plusDays(1)))
    }

    @Test
    fun `formatDaysUntil returns In N days for within a week`() {
        assertEquals("In 5 days", formatDaysUntil(LocalDate.now().plusDays(5)))
    }

    @Test
    fun `formatDaysUntil returns formatted date for more than 7 days`() {
        val date = LocalDate.now().plusDays(30)
        val result = formatDaysUntil(date)
        assertEquals(formatDate(date), result)
    }

    @Test
    fun `formatDaysUntil returns overdue for past dates`() {
        val result = formatDaysUntil(LocalDate.now().minusDays(3))
        assertTrue(result.contains("overdue"))
    }
}
