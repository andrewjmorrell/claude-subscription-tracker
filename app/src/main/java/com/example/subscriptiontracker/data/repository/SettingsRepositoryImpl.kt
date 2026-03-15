package com.example.subscriptiontracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.subscriptiontracker.domain.model.ReminderTiming
import com.example.subscriptiontracker.domain.model.UserSettings
import com.example.subscriptiontracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private companion object {
        val CURRENCY = stringPreferencesKey("currency_symbol")
        val REMINDER_TIMING = stringPreferencesKey("reminder_timing")
        val REMINDERS_ON = booleanPreferencesKey("reminders_enabled")
        val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
    }

    override fun getSettings(): Flow<UserSettings> = dataStore.data.map { p ->
        UserSettings(
            currencySymbol = p[CURRENCY] ?: "$",
            reminderTiming = p[REMINDER_TIMING]?.let { runCatching { ReminderTiming.valueOf(it) }.getOrNull() } ?: ReminderTiming.ONE_DAY_BEFORE,
            remindersEnabled = p[REMINDERS_ON] ?: true,
            darkModeEnabled = p[DARK_MODE] ?: false
        )
    }

    override suspend fun setCurrencySymbol(symbol: String) { dataStore.edit { it[CURRENCY] = symbol } }
    override suspend fun setReminderTiming(timing: String) { dataStore.edit { it[REMINDER_TIMING] = timing } }
    override suspend fun setRemindersEnabled(enabled: Boolean) { dataStore.edit { it[REMINDERS_ON] = enabled } }
    override suspend fun setDarkModeEnabled(enabled: Boolean) { dataStore.edit { it[DARK_MODE] = enabled } }
}
