package com.example.calculation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.calculation.ui.DepositActivity
import com.example.domain.interfaces.CalculationsNavigator
import com.example.domain.models.DepositCalculation

class CalculationsNavigatorImpl(private val context: Context) : CalculationsNavigator {

    override fun navigateToNewCalculation(userId: Long) {
        val intent = Intent(context, DepositActivity::class.java)
        intent.putExtra("mode", "new")
        intent.putExtra("userId", userId)
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(userId: Long) {
        val intent = Intent(context, DepositActivity::class.java)
        intent.putExtra("mode", "list")
        intent.putExtra("userId", userId)
        context.startActivity(intent)
    }

    override fun navigateToCalculationDetails(calculation: DepositCalculation) {
        val intent = Intent(context, DepositActivity::class.java)
        intent.putExtra("mode", "details")
        intent.putExtra("calculationId", calculation.id)
        intent.putExtra("userId", calculation.userId)
        context.startActivity(intent)
    }

    override fun openCalculationFlow(userId: Long) {
        val intent = Intent(context, DepositActivity::class.java)
        intent.putExtra("userId", userId)
        context.startActivity(intent)
    }
}