package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

data class AppSettings(
    val themeId: String = "dark",
    val vibrationEnabled: Boolean = true,
    val countdownSec: Int = 120,
    val alarmDurationSec: Int = 27,
    val appLockEnabled: Boolean = false,
    val appLockPin: String = "",
    val fingerprintEnabled: Boolean = false,
    val customAlarmName: String? = null
)

class SettingsRepository(private val context: Context) {
    private val THEME_ID = stringPreferencesKey("theme_id")
    private val VIBRATION = booleanPreferencesKey("vibration")
    private val COUNTDOWN_SEC = intPreferencesKey("countdown_sec")
    private val ALARM_DURATION_SEC = intPreferencesKey("alarm_duration_sec")
    private val APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
    private val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
    private val FINGERPRINT_ENABLED = booleanPreferencesKey("fingerprint_enabled")
    private val CUSTOM_ALARM_NAME = stringPreferencesKey("custom_alarm_name")

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        AppSettings(
            themeId = preferences[THEME_ID] ?: "dark",
            vibrationEnabled = preferences[VIBRATION] ?: true,
            countdownSec = preferences[COUNTDOWN_SEC] ?: 120,
            alarmDurationSec = preferences[ALARM_DURATION_SEC] ?: 27,
            appLockEnabled = preferences[APP_LOCK_ENABLED] ?: false,
            appLockPin = preferences[APP_LOCK_PIN] ?: "",
            fingerprintEnabled = preferences[FINGERPRINT_ENABLED] ?: false,
            customAlarmName = preferences[CUSTOM_ALARM_NAME]
        )
    }

    suspend fun updateThemeId(themeId: String) { context.dataStore.edit { it[THEME_ID] = themeId } }
    suspend fun updateVibration(enabled: Boolean) { context.dataStore.edit { it[VIBRATION] = enabled } }
    suspend fun updateCountdownSec(sec: Int) { context.dataStore.edit { it[COUNTDOWN_SEC] = sec } }
    suspend fun updateAlarmDurationSec(sec: Int) { context.dataStore.edit { it[ALARM_DURATION_SEC] = sec } }
    suspend fun updateAppLockEnabled(enabled: Boolean) { context.dataStore.edit { it[APP_LOCK_ENABLED] = enabled } }
    suspend fun updateAppLockPin(pin: String) { context.dataStore.edit { it[APP_LOCK_PIN] = pin } }
    suspend fun updateFingerprintEnabled(enabled: Boolean) { context.dataStore.edit { it[FINGERPRINT_ENABLED] = enabled } }
    suspend fun updateCustomAlarmName(name: String?) {
        context.dataStore.edit {
            if (name == null) it.remove(CUSTOM_ALARM_NAME) else it[CUSTOM_ALARM_NAME] = name
        }
    }
}
