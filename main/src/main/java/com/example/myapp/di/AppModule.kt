package com.example.myapp.di

import com.example.auth.di.authModule
import com.example.calculation.di.calculationsModule
import com.example.myapp.navigation.AuthNavigatorImpl
import com.example.myapp.navigation.CalculationsNavigatorImpl
import com.example.domain.navigation.AuthNavigator
import com.example.domain.navigation.CalculationsNavigator
import org.koin.dsl.module

val appModule = module {
    single<AuthNavigator> { AuthNavigatorImpl() }
    single<CalculationsNavigator> { CalculationsNavigatorImpl() }
}

val allModules = listOf(appModule, authModule, calculationsModule)