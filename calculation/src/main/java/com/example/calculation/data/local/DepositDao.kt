package com.example.calculation.data.local

import androidx.room.*
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationEntity>>

    @Insert
    suspend fun insertCalculation(entity: DepositCalculationEntity): Long

    @Delete
    suspend fun deleteCalculation(entity: DepositCalculationEntity)

    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)

    fun getDomainCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return getCalculationsForUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertDomainCalculation(calculation: DepositCalculation): Long {
        return insertCalculation(DepositCalculationEntity.fromDomain(calculation))
    }

    suspend fun deleteDomainCalculation(calculation: DepositCalculation) {
        deleteCalculation(DepositCalculationEntity.fromDomain(calculation))
    }
}