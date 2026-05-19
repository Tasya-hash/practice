package com.example.calculation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculation.data.repository.DepositRepository
import com.example.domain.models.CalculationResult
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepositViewModel(
    private val depositRepository: DepositRepository,
    private val userId: Long
) : ViewModel() {

    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    private val _calculationResult = MutableLiveData<CalculationResult?>()
    val calculationResult: LiveData<CalculationResult?> = _calculationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            depositRepository.getCalculationsForUser(userId)
                .collectLatest { calculations ->
                    _calculations.value = calculations
                    _isLoading.value = false
                }
        }
    }

    fun calculateDeposit(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ) {
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
    }

    fun saveCurrentCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ) {
        val result = calculationResult.value
        if (result == null) {
            _message.value = "Сначала выполните расчёт"
            return
        }

        viewModelScope.launch {
            val calculation = DepositCalculation(
                userId = userId,
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned,
                calculationDate = System.currentTimeMillis()
            )

            depositRepository.saveCalculation(calculation)
            _message.value = "Расчёт сохранён успешно"
            loadCalculations()
        }
    }

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            depositRepository.deleteCalculation(calculation)
            _message.value = "Расчёт удалён"
            loadCalculations()
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}