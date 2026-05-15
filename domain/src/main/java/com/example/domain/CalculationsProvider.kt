package com.example.domain

import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation): Long
    suspend fun deleteCalculation(calculationId: Long, userId: Long)
}