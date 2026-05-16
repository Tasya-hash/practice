package com.example.domain

interface CalculationsNavigator {
    fun navigateToNewCalculation(userId: Long)
    fun navigateToMyCalculations(userId: Long)
    fun openCalculationFlow(requestCode: Int, userId: Long)
    fun navigateToCalculationDetails(calculationId: Long, userId: Long)
}