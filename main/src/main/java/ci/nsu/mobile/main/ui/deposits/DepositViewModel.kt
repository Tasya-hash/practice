package ci.nsu.mobile.main.ui.deposits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.utils.TokenManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepositViewModel(
    private val depositRepository: DepositRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    private val _selectedCalculation = MutableLiveData<DepositCalculation?>()
    val selectedCalculation: LiveData<DepositCalculation?> = _selectedCalculation

    private val _calculationResult = MutableLiveData<CalculationResult?>()
    val calculationResult: LiveData<CalculationResult?> = _calculationResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val currentUserId: Long
        get() = tokenManager.getUserId()

    init {
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            depositRepository.getCalculationsForUser(currentUserId)
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
                userId = currentUserId,
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

    fun selectCalculation(calculation: DepositCalculation) {
        _selectedCalculation.value = calculation
    }

    fun clearSelectedCalculation() {
        _selectedCalculation.value = null
    }

    fun clearMessage() {
        _message.value = null
    }

    data class CalculationResult(
        val finalAmount: Double,
        val interestEarned: Double
    )
}