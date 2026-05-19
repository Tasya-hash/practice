package com.example.calculation.data.repository

import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation): Long
    suspend fun deleteCalculation(calculation: DepositCalculation)
    suspend fun deleteAllForUser(userId: Long)
}