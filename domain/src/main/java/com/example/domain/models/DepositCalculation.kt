package com.example.domain.models

import java.util.Date

data class DepositCalculation(
    val id: Long,
    val userId: Long,
    val amount: Double,
    val interestRate: Double,
    val termMonths: Int,
    val resultAmount: Double,
    val earnedInterest: Double,
    val dateCreated: Date
)