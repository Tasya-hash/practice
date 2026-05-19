package com.example.calculation.data.repository

import com.example.calculation.data.local.DepositDao
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepositoryImpl(private val depositDao: DepositDao) : DepositRepository {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getDomainCalculationsForUser(userId)
    }

    override suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return depositDao.insertDomainCalculation(calculation)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.deleteDomainCalculation(calculation)
    }

    override suspend fun deleteAllForUser(userId: Long) {
        depositDao.deleteAllForUser(userId)
    }
}