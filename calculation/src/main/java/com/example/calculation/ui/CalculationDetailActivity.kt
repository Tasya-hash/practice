package com.example.calculation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.calculation.databinding.ActivityCalculationDetailBinding
import com.example.calculation.viewmodel.DepositViewModel
import com.example.domain.managers.CalculationsProvider
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Locale

class CalculationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculationDetailBinding
    private val calculationsProvider: CalculationsProvider by inject()
    private val depositViewModel: DepositViewModel by inject()
    private var calculationId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calculationId = intent.getLongExtra("CALCULATION_ID", 0)

        if (calculationId == 0L) {
            Toast.makeText(this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadCalculationDetails()
        setupClickListeners()
    }

    private fun loadCalculationDetails() {
        lifecycleScope.launch {
            val calculation = calculationsProvider.getCalculationById(calculationId)
            if (calculation != null) {
                displayCalculation(calculation)
            } else {
                Toast.makeText(this@CalculationDetailActivity, "Расчет не найден", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayCalculation(calculation: com.example.domain.models.DepositCalculation) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

        binding.tvAmount.text = String.format("%.2f ₽", calculation.amount)
        binding.tvInterestRate.text = String.format("%.1f %%", calculation.interestRate)
        binding.tvTermMonths.text = String.format("%d месяцев", calculation.termMonths)
        binding.tvResultAmount.text = String.format("%.2f ₽", calculation.resultAmount)
        binding.tvEarnedInterest.text = String.format("%.2f ₽", calculation.earnedInterest)
        binding.tvDateCreated.text = dateFormat.format(calculation.dateCreated)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Удаление расчета")
            .setMessage("Вы уверены, что хотите удалить этот расчет?")
            .setPositiveButton("Удалить") { _, _ ->
                deleteCalculation()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteCalculation() {
        lifecycleScope.launch {
            try {
                calculationsProvider.deleteCalculation(calculationId)
                Toast.makeText(this@CalculationDetailActivity, "Расчет удален", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CalculationDetailActivity, "Ошибка при удалении", Toast.LENGTH_SHORT).show()
            }
        }
    }
}