package com.example.subscriptiontracker.domain.repository

import com.example.subscriptiontracker.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<UserSettings>
    suspend fun setCurrencySymbol(symbol: String)
    suspend fun setReminderTiming(timing: String)
    suspend fun setRemindersEnabled(enabled: Boolean)
    suspend fun setDarkModeEnabled(enabled: Boolean)
}
