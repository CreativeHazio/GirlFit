package com.creativehazio.common.platform

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual fun dataStoreModule(): Module = module {
    single<DataStore<Preferences>> {
        val context = get<Context>()

        createDataStore(
            producePath = {
                File(context.filesDir, dataStoreFileName).absolutePath.toPath()
            }
        )
    }
}