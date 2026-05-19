package com.example.calculation

import com.example.calculation.data.repository.DepositRepository
import com.example.domain.interfaces.CalculationsProvider
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class CalculationsProviderImpl(
    private val depositRepository: DepositRepository
) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositRepository.getCalculationsForUser(userId)
    }

    override suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return depositRepository.saveCalculation(calculation)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositRepository.deleteCalculation(calculation)
    }

    override suspend fun deleteAllForUser(userId: Long) {
        depositRepository.deleteAllForUser(userId)
    }
}