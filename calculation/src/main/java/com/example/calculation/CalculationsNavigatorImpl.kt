package com.example.calculation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.calculation.ui.deposits.DepositActivity
import com.example.domain.CalculationsNavigator
class CalculationsNavigatorImpl(private val context: Context) : CalculationsNavigator {

    override fun navigateToNewCalculation(userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "new")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "list")
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }

    override fun openCalculationFlow(requestCode: Int, userId: Long) {
        if (context is Activity) {
            val intent = Intent(context, DepositActivity::class.java).apply {
                putExtra("userId", userId)
            }
            context.startActivityForResult(intent, requestCode)
        }
    }

    override fun navigateToCalculationDetails(calculationId: Long, userId: Long) {
        val intent = Intent(context, DepositActivity::class.java).apply {
            putExtra("mode", "details")
            putExtra("calculationId", calculationId)
            putExtra("userId", userId)
        }
        context.startActivity(intent)
    }
}