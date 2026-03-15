package com.example.subscriptiontracker.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.mostExpensive
import com.example.subscriptiontracker.util.totalMonthlyCost
import com.example.subscriptiontracker.util.totalYearlyCost
import com.example.subscriptiontracker.util.upcomingBills
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * UI state for the Dashboard screen.
 *
 * @property activeCount Total number of active subscriptions.
 * @property monthlyCost Aggregated monthly cost of all active subscriptions.
 * @property yearlyCost Aggregated yearly cost of all active subscriptions.
 * @property upcomingBills Subscriptions billing within the next 7 days.
 * @property topExpensive The top 5 most expensive active subscriptions.
 */
data class DashboardUiState(
    val activeCount: Int = 0,
    val monthlyCost: Double = 0.0,
    val yearlyCost: Double = 0.0,
    val upcomingBills: List<Subscription> = emptyList(),
    val topExpensive: List<Subscription> = emptyList()
)

/**
 * ViewModel for the Dashboard screen.
 * Aggregates active subscription data into summary metrics displayed on the home screen.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = subscriptionRepository.getActive()
        .map { subs ->
            DashboardUiState(
                activeCount = subs.size,
                monthlyCost = totalMonthlyCost(subs),
                yearlyCost = totalYearlyCost(subs),
                upcomingBills = upcomingBills(subs, days = 7),
                topExpensive = mostExpensive(subs, limit = 5)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())
}
