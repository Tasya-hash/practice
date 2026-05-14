package ci.nsu.mobile.main.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId AND id = :id")
    suspend fun getCalculationById(userId: Long, id: Long): DepositCalculation?

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation): Long

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculation)

    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId AND calculationDate BETWEEN :startDate AND :endDate")
    fun getCalculationsInDateRange(userId: Long, startDate: Long, endDate: Long): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId AND initialAmount BETWEEN :minAmount AND :maxAmount")
    fun getCalculationsByAmountRange(userId: Long, minAmount: Double, maxAmount: Double): Flow<List<DepositCalculation>>
}