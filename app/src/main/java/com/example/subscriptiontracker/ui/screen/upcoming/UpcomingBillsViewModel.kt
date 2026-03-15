package com.example.subscriptiontracker.ui.screen.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.upcomingBills
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * UI state for the Upcoming Bills screen.
 *
 * @property bills Subscriptions with billing dates within the next 30 days, sorted by date.
 * @property totalDue Total cost of all upcoming bills.
 */
data class UpcomingBillsUiState(
    val bills: List<Subscription> = emptyList(),
    val totalDue: Double = 0.0
)

/**
 * ViewModel for the Upcoming Bills screen.
 * Filters active subscriptions to those billing within the next 30 days.
 */
@HiltViewModel
class UpcomingBillsViewModel @Inject constructor(
    subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    val uiState: StateFlow<UpcomingBillsUiState> = subscriptionRepository.getActive()
        .map { subs ->
            val upcoming = upcomingBills(subs, days = 30)
            UpcomingBillsUiState(
                bills = upcoming,
                totalDue = upcoming.sumOf { it.amount }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UpcomingBillsUiState())
}
