package com.example.domain.interfaces

import com.example.domain.models.DepositCalculation

interface CalculationsNavigator {
    fun navigateToNewCalculation(userId: Long)
    fun navigateToMyCalculations(userId: Long)
    fun navigateToCalculationDetails(calculation: DepositCalculation)
    fun openCalculationFlow(userId: Long)
}