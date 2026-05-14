package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.local.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepositoryImpl(private val depositDao: DepositDao) : DepositRepository {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId)
    }

    override suspend fun saveCalculation(calculation: DepositCalculation): Long {
        return depositDao.insertCalculation(calculation)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.deleteCalculation(calculation)
    }

    override suspend fun deleteAllForUser(userId: Long) {
        depositDao.deleteAllForUser(userId)
    }
}