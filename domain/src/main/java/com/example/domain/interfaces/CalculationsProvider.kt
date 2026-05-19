package com.example.domain.interfaces

import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation): Long
    suspend fun deleteCalculation(calculation: DepositCalculation)
    suspend fun deleteAllForUser(userId: Long)
}