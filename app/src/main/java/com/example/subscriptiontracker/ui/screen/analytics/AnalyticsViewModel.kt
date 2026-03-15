package com.example.subscriptiontracker.ui.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.CategorySpending
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.categorySpending
import com.example.subscriptiontracker.util.totalMonthlyCost
import com.example.subscriptiontracker.util.totalYearlyCost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * UI state for the Analytics screen.
 *
 * @property totalMonthly Total monthly cost across all active subscriptions.
 * @property totalYearly Total yearly cost across all active subscriptions.
 * @property categoryBreakdown Spending aggregated by category, sorted by monthly total descending.
 * @property activeCount Number of active subscriptions.
 */
data class AnalyticsUiState(
    val totalMonthly: Double = 0.0,
    val totalYearly: Double = 0.0,
    val categoryBreakdown: List<CategorySpending> = emptyList(),
    val activeCount: Int = 0
)

/**
 * ViewModel for the Analytics screen.
 * Computes spending analytics: totals, per-category breakdowns, and percentages.
 */
@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    val uiState: StateFlow<AnalyticsUiState> = subscriptionRepository.getActive()
        .map { subs ->
            AnalyticsUiState(
                totalMonthly = totalMonthlyCost(subs),
                totalYearly = totalYearlyCost(subs),
                categoryBreakdown = categorySpending(subs),
                activeCount = subs.size
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AnalyticsUiState())
}
