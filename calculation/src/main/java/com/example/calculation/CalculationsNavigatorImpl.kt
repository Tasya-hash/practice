package com.example.calculation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.calculation.ui.deposits.DepositActivity
import com.example.domain.CalculationsNavigator

class CalculationsNavigatorImpl : CalculationsNavigator {

    override fun navigateToNewCalculation(context: Context, userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "new")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "list")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        val intent = Intent(activity, DepositActivity::class.java).apply {
            putExtra("userId", userId)
        }
        activity.startActivity(intent)
    }

    override fun navigateToCalculationDetails(context: Context, calculationId: Long, userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "details")
            putExtra("calculationId", calculationId)
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }
}