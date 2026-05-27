package com.creativehazio.data.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.creativehazio.data.localdb.GirlFitDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val DATABASE_PATH = "girlfit.db"

actual fun dataPlatformModule(): Module {
    return module {
        single {
            getAppDatabase()
        }
    }
}

fun getAppDatabase() : GirlFitDatabase {
    val dbFilePath = documentDirectory() + "/${DATABASE_PATH}"

    return Room.databaseBuilder<GirlFitDatabase>(
        name = dbFilePath
    ).setDriver(BundledSQLiteDriver())
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory() : String {
    val documentDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDir?.path)
}