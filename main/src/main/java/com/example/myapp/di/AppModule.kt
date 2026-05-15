package com.example.myapp.di

import com.example.domain.navigation.AuthNavigator
import com.example.domain.navigation.CalculationsNavigator
import com.example.practice.navigation.AuthNavigatorImpl
import com.example.practice.navigation.CalculationsNavigatorImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthNavigator> { AuthNavigatorImpl() }
    single<CalculationsNavigator> { CalculationsNavigatorImpl() }
}