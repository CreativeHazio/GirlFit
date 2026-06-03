package com.creativehazio.common.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(
    private val dataStore: DataStore<Preferences>
) {

    private val IS_ONBOARDED_KEY = booleanPreferencesKey("is_onboarded")

    suspend fun saveOnboardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDED_KEY] = completed
        }
    }

    fun isOnboarded(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_ONBOARDED_KEY] ?: false
    }

}