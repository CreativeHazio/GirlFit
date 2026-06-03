package com.creativehazio.data.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.creativehazio.data.localdb.GirlFitDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

private const val DATABASE_PATH = "girlfit.db"

actual fun dataPlatformModule(): Module {
    return module {
        single {
            getAppDatabase(get())
        }
    }
}

internal fun getAppDatabase(context: Context) : GirlFitDatabase {
    val dbFile = context.getDatabasePath(DATABASE_PATH)

    return Room.databaseBuilder<GirlFitDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}