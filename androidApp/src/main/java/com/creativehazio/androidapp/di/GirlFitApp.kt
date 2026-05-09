package com.creativehazio.androidapp.di

import android.app.Application
import com.creativehazio.girlfit.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GirlFitApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@GirlFitApp)
            androidLogger()
        }
    }

}