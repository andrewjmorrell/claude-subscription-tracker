package com.example.subscriptiontracker.ui.screen.subscriptionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.SortOption
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.filterSubscriptions
import com.example.subscriptiontracker.util.sortSubscriptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Subscription List screen.
 *
 * @property subscriptions The filtered and sorted list of subscriptions to display.
 * @property searchQuery Current search text filter.
 * @property sortOption Current sort order.
 * @property categoryFilter Optional category filter; null means show all.
 */
data class SubscriptionListUiState(
    val subscriptions: List<Subscription> = emptyList(),
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NAME,
    val categoryFilter: Category? = null
)

/**
 * ViewModel for the Subscription List screen.
 * Provides search, sort, filter, delete, and undo-delete capabilities.
 */
@HiltViewModel
class SubscriptionListViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(SortOption.NAME)
    private val categoryFilter = MutableStateFlow<Category?>(null)
    private var lastDeletedSubscription: Subscription? = null

    val uiState: StateFlow<SubscriptionListUiState> = combine(
        subscriptionRepository.getAll(),
        searchQuery,
        sortOption,
        categoryFilter
    ) { subs, query, sort, cat ->
        val filtered = filterSubscriptions(subs, query, cat)
        val sorted = sortSubscriptions(filtered, sort)
        SubscriptionListUiState(
            subscriptions = sorted,
            searchQuery = query,
            sortOption = sort,
            categoryFilter = cat
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SubscriptionListUiState())

    /** Updates the search query filter. */
    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    /** Updates the sort option. */
    fun onSortOptionChange(option: SortOption) {
        sortOption.value = option
    }

    /** Sets or clears the category filter. */
    fun onCategoryFilterChange(category: Category?) {
        categoryFilter.value = category
    }

    /** Deletes a subscription and stores it for potential undo. */
    fun deleteSubscription(subscription: Subscription) {
        lastDeletedSubscription = subscription
        viewModelScope.launch {
            subscriptionRepository.delete(subscription)
        }
    }

    /** Restores the last deleted subscription. */
    fun undoDelete() {
        val sub = lastDeletedSubscription ?: return
        lastDeletedSubscription = null
        viewModelScope.launch {
            subscriptionRepository.insert(sub)
        }
    }
}
