package com.example.calculation.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId AND id = :id")
    suspend fun getCalculationById(userId: Long, id: Long): DepositCalculationEntity?

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculationEntity): Long

    @Query("DELETE FROM deposit_calculations WHERE id = :calculationId AND userId = :userId")
    suspend fun deleteCalculationById(calculationId: Long, userId: Long)

    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)
}