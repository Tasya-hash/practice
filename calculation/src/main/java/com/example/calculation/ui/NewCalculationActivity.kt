package com.example.calculation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.calculation.databinding.ActivityNewCalculationBinding
import com.example.calculation.viewmodel.DepositViewModel
import com.example.domain.managers.AuthManager
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Date

class NewCalculationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewCalculationBinding
    private val depositViewModel: DepositViewModel by inject()
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnCalculate.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val rate = binding.etInterestRate.text.toString().toDoubleOrNull()
            val term = binding.etTermMonths.text.toString().toIntOrNull()

            if (amount != null && rate != null && term != null) {
                calculateDeposit(amount, rate, term)
            } else {
                Toast.makeText(this, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSave.setOnClickListener {
            val resultText = binding.tvResult.text.toString()
            if (resultText.isNotEmpty() && resultText != "Результат появится здесь") {
                saveCalculation()
            } else {
                Toast.makeText(this, "Сначала выполните расчет", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateDeposit(amount: Double, rate: Double, termMonths: Int) {
        val monthlyRate = rate / 100 / 12
        val resultAmount = amount * Math.pow(1 + monthlyRate, termMonths.toDouble())
        val earnedInterest = resultAmount - amount

        binding.tvResult.text = String.format(
            "Сумма вклада: %.2f\nПроцентная ставка: %.1f%%\nСрок: %d мес.\n\nИтоговая сумма: %.2f\nЗаработано процентов: %.2f",
            amount, rate, termMonths, resultAmount, earnedInterest
        )

        // Сохраняем результат для сохранения
        currentCalculation = DepositCalculation(
            userId = authManager.getUserId() ?: 1,
            amount = amount,
            interestRate = rate,
            termMonths = termMonths,
            resultAmount = resultAmount,
            earnedInterest = earnedInterest,
            dateCreated = Date()
        )
    }

    private var currentCalculation: DepositCalculation? = null

    private fun saveCalculation() {
        lifecycleScope.launch {
            currentCalculation?.let { calculation ->
                depositViewModel.saveCalculation(calculation)
                Toast.makeText(this@NewCalculationActivity, "Расчет сохранен!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}