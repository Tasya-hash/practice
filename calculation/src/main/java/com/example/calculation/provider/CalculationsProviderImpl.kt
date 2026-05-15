package com.example.calculation.provider

import com.example.calculations.database.DepositDao
import com.example.calculations.database.DepositCalculationEntity
import com.example.domain.managers.CalculationsProvider
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(
    private val depositDao: DepositDao
) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation.toEntity())
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        depositDao.deleteCalculation(calculationId)
    }

    override suspend fun getCalculationById(calculationId: Long): DepositCalculation? {
        return depositDao.getCalculationById(calculationId)?.toDomainModel()
    }

    private fun DepositCalculationEntity.toDomainModel() = DepositCalculation(
        id = id,
        userId = userId,
        amount = amount,
        interestRate = interestRate,
        termMonths = termMonths,
        resultAmount = resultAmount,
        earnedInterest = earnedInterest,
        dateCreated = dateCreated
    )

    private fun DepositCalculation.toEntity() = DepositCalculationEntity(
        id = id,
        userId = userId,
        amount = amount,
        interestRate = interestRate,
        termMonths = termMonths,
        resultAmount = resultAmount,
        earnedInterest = earnedInterest,
        dateCreated = dateCreated
    )
}