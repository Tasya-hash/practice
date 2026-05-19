package com.example.calculation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
) {
    fun toDomain(): com.example.domain.models.DepositCalculation {
        return com.example.domain.models.DepositCalculation(
            id = id,
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = calculationDate
        )
    }

    companion object {
        fun fromDomain(calculation: com.example.domain.models.DepositCalculation): DepositCalculationEntity {
            return DepositCalculationEntity(
                id = calculation.id,
                userId = calculation.userId,
                initialAmount = calculation.initialAmount,
                periodMonths = calculation.periodMonths,
                interestRate = calculation.interestRate,
                monthlyTopUp = calculation.monthlyTopUp,
                finalAmount = calculation.finalAmount,
                interestEarned = calculation.interestEarned,
                calculationDate = calculation.calculationDate
            )
        }
    }
}