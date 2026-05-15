package com.example.calculation.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY dateCreated DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculationEntity?

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculationEntity)

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculationEntity)

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteCalculationById(id: Long)
}