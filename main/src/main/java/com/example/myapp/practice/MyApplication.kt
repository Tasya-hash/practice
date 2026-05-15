package com.example.myapp.practice

import android.app.Application
import com.example.auth.di.authModule
import com.example.calculation.di.calculationsModule
import com.example.practice.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, authModule, calculationsModule)
        }
    }
}