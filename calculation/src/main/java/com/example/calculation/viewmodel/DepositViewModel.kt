package com.example.calculation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.managers.CalculationsProvider
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.launch

class DepositViewModel(
    private val calculationsProvider: CalculationsProvider
) : ViewModel() {

    suspend fun saveCalculation(calculation: DepositCalculation) {
        calculationsProvider.saveCalculation(calculation)
    }

    fun getCalculationsForUser(userId: Long) = calculationsProvider.getCalculationsForUser(userId)
}