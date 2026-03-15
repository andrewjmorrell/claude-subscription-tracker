package com.example.subscriptiontracker.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subscriptiontracker.domain.model.ReminderTiming
import com.example.subscriptiontracker.domain.model.UserSettings
import com.example.subscriptiontracker.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 * Reads and writes user preferences via [SettingsRepository].
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepository.getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    /** Updates the currency symbol preference. */
    fun setCurrencySymbol(symbol: String) {
        viewModelScope.launch { settingsRepository.setCurrencySymbol(symbol) }
    }

    /** Updates the reminder timing preference. */
    fun setReminderTiming(timing: ReminderTiming) {
        viewModelScope.launch { settingsRepository.setReminderTiming(timing.name) }
    }

    /** Toggles reminders on or off. */
    fun setRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setRemindersEnabled(enabled) }
    }

    /** Toggles dark mode on or off. */
    fun setDarkModeEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setDarkModeEnabled(enabled) }
    }
}
