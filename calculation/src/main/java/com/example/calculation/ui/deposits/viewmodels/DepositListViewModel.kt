package com.example.calculation.ui.deposits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculation.CalculationsProviderImpl
import com.example.calculation.data.local.DepositDatabase
import com.example.domain.DepositCalculation
import kotlinx.coroutines.launch

class DepositListViewModel(
    private val userId: Long
) : ViewModel() {

    private lateinit var calculationsProvider: CalculationsProviderImpl

    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun init(database: DepositDatabase) {
        calculationsProvider = CalculationsProviderImpl(database)
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            calculationsProvider.getCalculationsForUser(userId).collect { calculations ->
                _calculations.value = calculations
                _isLoading.value = false
            }
        }
    }

    fun deleteCalculation(calculationId: Long) {
        viewModelScope.launch {
            calculationsProvider.deleteCalculation(calculationId, userId)
            _message.value = "Расчёт удалён"
            loadCalculations()
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}