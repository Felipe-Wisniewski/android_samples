package com.fwisniewski.firebasesample

import android.app.Application
import com.fwisniewski.firebasesample.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(androidModule)
        }
    }
}