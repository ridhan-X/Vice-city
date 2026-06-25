package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.ContactRepository
import com.example.data.SettingsRepository
import com.example.util.LocationHelper

class AyuGuardApplication : Application() {
    lateinit var database: AppDatabase
    lateinit var contactRepository: ContactRepository
    lateinit var settingsRepository: SettingsRepository
    lateinit var locationHelper: LocationHelper

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "ayuguard_db").build()
        contactRepository = ContactRepository(database.contactDao())
        settingsRepository = SettingsRepository(this)
        locationHelper = LocationHelper(this)
    }
}
