package com.example.calculation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculation.databinding.ActivityMyCalculationsBinding
import com.example.calculation.viewmodel.DepositViewModel
import com.example.domain.managers.AuthManager
import com.example.domain.models.DepositCalculation
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyCalculationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCalculationsBinding
    private val depositViewModel: DepositViewModel by inject()
    private val authManager: AuthManager by inject()
    private lateinit var adapter: CalculationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCalculationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadCalculations()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = CalculationsAdapter { calculation ->
            // При клике открываем детали расчета
            val intent = android.content.Intent(this, CalculationDetailActivity::class.java)
            intent.putExtra("CALCULATION_ID", calculation.id)
            startActivity(intent)
        }
        binding.recyclerViewCalculations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCalculations.adapter = adapter
    }

    private fun loadCalculations() {
        val userId = authManager.getUserId() ?: return

        lifecycleScope.launch {
            depositViewModel.getCalculationsForUser(userId).collect { calculations ->
                if (calculations.isEmpty()) {
                    binding.tvEmpty.visibility = android.view.View.VISIBLE
                    binding.recyclerViewCalculations.visibility = android.view.View.GONE
                } else {
                    binding.tvEmpty.visibility = android.view.View.GONE
                    binding.recyclerViewCalculations.visibility = android.view.View.VISIBLE
                    adapter.submitList(calculations)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}