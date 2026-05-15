package com.example.calculation.di

import android.content.Context
import com.example.calculation.database.DepositDatabase
import com.example.calculation.provider.CalculationsProviderImpl
import com.example.calculation.viewmodel.DepositViewModel
import com.example.domain.managers.CalculationsProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calculationsModule = module {
    single { DepositDatabase.getDatabase(androidContext()) }
    single { get<DepositDatabase>().depositDao() }
    single<CalculationsProvider> { CalculationsProviderImpl(get()) }
    viewModel { DepositViewModel(get()) }
}