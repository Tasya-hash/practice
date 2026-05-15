package com.example.myapp.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.calculation.ui.MyCalculationsActivity
import com.example.calculation.ui.NewCalculationActivity
import com.example.calculation.ui.CalculationDetailActivity
import com.example.domain.navigation.CalculationsNavigator

class CalculationsNavigatorImpl : CalculationsNavigator {
    override fun navigateToNewCalculation(context: Context, userId: Long) {
        val intent = Intent(context, NewCalculationActivity::class.java).apply {
            putExtra("USER_ID", userId)
        }
        context.startActivity(intent)
    }

    override fun navigateToMyCalculations(context: Context, userId: Long) {
        val intent = Intent(context, MyCalculationsActivity::class.java).apply {
            putExtra("USER_ID", userId)
        }
        context.startActivity(intent)
    }

    override fun openCalculationFlow(activity: Activity, userId: Long) {
        val intent = Intent(activity, NewCalculationActivity::class.java).apply {
            putExtra("USER_ID", userId)
        }
        activity.startActivityForResult(intent, CALCULATION_REQUEST_CODE)
    }

    override fun navigateToCalculationDetail(context: Context, calculationId: Long) {
        val intent = Intent(context, CalculationDetailActivity::class.java).apply {
            putExtra("CALCULATION_ID", calculationId)
        }
        context.startActivity(intent)
    }

    companion object {
        private const val CALCULATION_REQUEST_CODE = 2001
    }
}