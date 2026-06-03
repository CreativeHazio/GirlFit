package com.creativehazio.common.platform

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path
import org.koin.core.module.Module

internal const val dataStoreFileName = "girlfit.preferences_pb"

fun createDataStore(producePath: () -> Path): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = producePath
    )
}

expect fun dataStoreModule(): Module