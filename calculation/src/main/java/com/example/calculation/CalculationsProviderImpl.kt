package com.example.calculation

import com.example.calculation.data.local.DepositCalculationEntity
import com.example.calculation.data.local.DepositDao
import com.example.calculation.data.local.DepositDatabase
import com.example.domain.CalculationsProvider
import com.example.domain.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(
    private val database: DepositDatabase
) : CalculationsProvider {

    private val depositDao: DepositDao = database.depositDao()

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return depositDao.insertCalculation(calculation.toEntity())
    }

    override suspend fun deleteCalculation(calculationId: Long, userId: Long) {
        depositDao.deleteCalculationById(calculationId, userId)
    }

    private fun DepositCalculationEntity.toDomain(): DepositCalculation {
        return DepositCalculation(
            id = this.id,
            userId = this.userId,
            initialAmount = this.initialAmount,
            periodMonths = this.periodMonths,
            interestRate = this.interestRate,
            monthlyTopUp = this.monthlyTopUp,
            finalAmount = this.finalAmount,
            interestEarned = this.interestEarned,
            calculationDate = this.calculationDate
        )
    }

    private fun DepositCalculation.toEntity(): DepositCalculationEntity {
        return DepositCalculationEntity(
            id = this.id,
            userId = this.userId,
            initialAmount = this.initialAmount,
            periodMonths = this.periodMonths,
            interestRate = this.interestRate,
            monthlyTopUp = this.monthlyTopUp,
            finalAmount = this.finalAmount,
            interestEarned = this.interestEarned,
            calculationDate = this.calculationDate
        )
    }
}