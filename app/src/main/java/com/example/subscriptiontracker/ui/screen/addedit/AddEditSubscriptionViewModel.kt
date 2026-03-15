package com.example.subscriptiontracker.ui.screen.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.BillingCycle
import com.example.subscriptiontracker.domain.model.Category
import com.example.subscriptiontracker.domain.model.Subscription
import com.example.subscriptiontracker.domain.repository.SubscriptionRepository
import com.example.subscriptiontracker.util.validateSubscription
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Mutable form state for the Add/Edit Subscription screen.
 *
 * @property name The subscription name.
 * @property amount The subscription amount as a string (for text field binding).
 * @property billingCycle The selected billing frequency.
 * @property category The selected category.
 * @property nextBillingDate The next billing date, or null if not yet set.
 * @property trialEndDate Optional trial end date.
 * @property notes Optional notes.
 * @property isActive Whether the subscription is currently active.
 * @property errors Map of field name to error message for validation.
 * @property isEditing Whether we are editing an existing subscription (vs creating new).
 * @property isSaved Set to true after successful save to trigger navigation back.
 */
data class AddEditUiState(
    val name: String = "",
    val amount: String = "",
    val billingCycle: BillingCycle = BillingCycle.MONTHLY,
    val category: Category = Category.OTHER,
    val nextBillingDate: LocalDate? = null,
    val trialEndDate: LocalDate? = null,
    val notes: String = "",
    val isActive: Boolean = true,
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val isSaved: Boolean = false
)

/**
 * ViewModel for the Add/Edit Subscription screen.
 * Loads existing subscription data when editing, validates input, and persists changes.
 */
@HiltViewModel
class AddEditSubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    private val subscriptionId: Long? = savedStateHandle.get<Long>("id")

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    private var originalSubscription: Subscription? = null

    init {
        if (subscriptionId != null && subscriptionId > 0) {
            viewModelScope.launch {
                subscriptionRepository.getById(subscriptionId)?.let { sub ->
                    originalSubscription = sub
                    _uiState.value = AddEditUiState(
                        name = sub.name,
                        amount = sub.amount.toString(),
                        billingCycle = sub.billingCycle,
                        category = sub.category,
                        nextBillingDate = sub.nextBillingDate,
                        trialEndDate = sub.trialEndDate,
                        notes = sub.notes,
                        isActive = sub.isActive,
                        isEditing = true
                    )
                }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onAmountChange(value: String) = _uiState.update { it.copy(amount = value) }
    fun onBillingCycleChange(value: BillingCycle) = _uiState.update { it.copy(billingCycle = value) }
    fun onCategoryChange(value: Category) = _uiState.update { it.copy(category = value) }
    fun onNextBillingDateChange(value: LocalDate) = _uiState.update { it.copy(nextBillingDate = value) }
    fun onTrialEndDateChange(value: LocalDate?) = _uiState.update { it.copy(trialEndDate = value) }
    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }
    fun onActiveChange(value: Boolean) = _uiState.update { it.copy(isActive = value) }

    /**
     * Validates the form and saves the subscription if valid.
     * Sets [AddEditUiState.isSaved] to true on success, or populates
     * [AddEditUiState.errors] on validation failure.
     */
    fun save() {
        val state = _uiState.value
        val errors = validateSubscription(state.name, state.amount, state.nextBillingDate)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }

        viewModelScope.launch {
            val now = LocalDateTime.now()
            val subscription = Subscription(
                id = originalSubscription?.id ?: 0,
                name = state.name.trim(),
                amount = state.amount.toDouble(),
                billingCycle = state.billingCycle,
                category = state.category,
                nextBillingDate = state.nextBillingDate!!,
                trialEndDate = state.trialEndDate,
                notes = state.notes.trim(),
                isActive = state.isActive,
                createdAt = originalSubscription?.createdAt ?: now,
                updatedAt = now
            )

            if (state.isEditing) {
                subscriptionRepository.update(subscription)
            } else {
                subscriptionRepository.insert(subscription)
            }

            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
