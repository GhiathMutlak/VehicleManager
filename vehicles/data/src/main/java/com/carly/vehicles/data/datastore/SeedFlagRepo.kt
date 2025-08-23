package com.carly.vehicles.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val CATALOG_SEEDED = booleanPreferencesKey("catalog_seeded")

@Singleton
class SeedFlagRepo @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun isCatalogSeeded(): Boolean {
        return dataStore.data.first()[CATALOG_SEEDED] ?: false
    }

    suspend fun setCatalogSeeded() {
        dataStore.edit { prefs ->
            prefs[CATALOG_SEEDED] = true
        }
    }
}