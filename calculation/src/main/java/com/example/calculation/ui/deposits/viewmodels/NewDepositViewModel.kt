package com.example.calculation.ui.deposits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculation.CalculationsProviderImpl
import com.example.calculation.data.local.DepositDatabase
import com.example.domain.DepositCalculation
import kotlinx.coroutines.launch

class NewDepositViewModel(
    private val userId: Long
) : ViewModel() {

    private lateinit var calculationsProvider: CalculationsProviderImpl

    private val _calculationResult = MutableLiveData<CalculationResult?>()
    val calculationResult: LiveData<CalculationResult?> = _calculationResult

    private val _isCalculating = MutableLiveData<Boolean>()
    val isCalculating: LiveData<Boolean> = _isCalculating

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private var currentCalculation: Triple<Double, Int, Double>? = null
    private var currentMonthlyTopUp: Double? = null

    fun init(database: DepositDatabase) {
        calculationsProvider = CalculationsProviderImpl(database)
    }

    fun calculateDeposit(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ) {
        _isCalculating.value = true

        currentCalculation = Triple(initialAmount, periodMonths, interestRate)
        currentMonthlyTopUp = monthlyTopUp

        val monthlyRate = interestRate / 12 / 100
        var amount = initialAmount
        var totalInterest = 0.0

        for (month in 1..periodMonths) {
            val monthlyInterest = amount * monthlyRate
            totalInterest += monthlyInterest
            amount += monthlyInterest

            monthlyTopUp?.let {
                amount += it
            }
        }

        _calculationResult.value = CalculationResult(
            finalAmount = amount,
            interestEarned = totalInterest
        )
        _isCalculating.value = false
    }

    fun saveCalculation() {
        val result = _calculationResult.value
        val calculation = currentCalculation

        if (result == null || calculation == null) {
            _message.value = "Сначала выполните расчёт"
            return
        }

        viewModelScope.launch {
            val depositCalculation = DepositCalculation(
                userId = userId,
                initialAmount = calculation.first,
                periodMonths = calculation.second,
                interestRate = calculation.third,
                monthlyTopUp = currentMonthlyTopUp,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned,
                calculationDate = System.currentTimeMillis()
            )

            calculationsProvider.saveCalculation(depositCalculation)
            _isSaved.value = true
            _message.value = "Расчёт сохранён успешно"
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun resetSaved() {
        _isSaved.value = false
    }

    data class CalculationResult(
        val finalAmount: Double,
        val interestEarned: Double
    )
}