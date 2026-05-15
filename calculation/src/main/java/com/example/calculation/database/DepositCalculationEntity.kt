package com.example.calculation.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val interestRate: Double,
    val termMonths: Int,
    val resultAmount: Double,
    val earnedInterest: Double,
    val dateCreated: Date = Date()
)