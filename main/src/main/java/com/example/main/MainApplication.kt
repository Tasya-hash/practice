package com.example.main

import android.app.Application
import android.content.Context
import com.example.auth.navigation.AuthNavigatorImpl
import com.example.calculation.navigation.CalculationsNavigatorImpl
import com.example.domain.interfaces.AuthNavigator
import com.example.domain.interfaces.CalculationsNavigator

class MainApplication : Application() {

    lateinit var authNavigator: AuthNavigator
    lateinit var calculationsNavigator: CalculationsNavigator

    override fun onCreate() {
        super.onCreate()
        instance = this

        authNavigator = AuthNavigatorImpl(this)
        calculationsNavigator = CalculationsNavigatorImpl(this)
    }

    companion object {
        lateinit var instance: MainApplication
            private set

        fun getAppContext(): Context = instance.applicationContext
    }
}

interface AuthProvider {
    fun getAuthNavigator(): AuthNavigator
}