package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppSettings
import com.example.data.Contact
import com.example.data.ContactRepository
import com.example.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val contactRepository: ContactRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val contacts: StateFlow<List<Contact>> = contactRepository.allContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AppSettings> = settingsRepository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppSettings())

    fun addContact(name: String, phone: String) {
        viewModelScope.launch {
            contactRepository.insert(Contact(name = name, phone = phone))
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteById(contact.id)
        }
    }

    fun updateThemeId(themeId: String) = viewModelScope.launch { settingsRepository.updateThemeId(themeId) }
    fun updateVibration(enabled: Boolean) = viewModelScope.launch { settingsRepository.updateVibration(enabled) }
    fun updateCountdownSec(sec: Int) = viewModelScope.launch { settingsRepository.updateCountdownSec(sec) }
    fun updateAlarmDurationSec(sec: Int) = viewModelScope.launch { settingsRepository.updateAlarmDurationSec(sec) }
    fun updateAppLockEnabled(enabled: Boolean) = viewModelScope.launch { settingsRepository.updateAppLockEnabled(enabled) }
    fun updateAppLockPin(pin: String) = viewModelScope.launch { settingsRepository.updateAppLockPin(pin) }
    fun updateFingerprintEnabled(enabled: Boolean) = viewModelScope.launch { settingsRepository.updateFingerprintEnabled(enabled) }
}
