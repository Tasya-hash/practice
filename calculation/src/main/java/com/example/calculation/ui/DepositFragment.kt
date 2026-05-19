package com.example.calculation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculation.adapters.DepositAdapter
import com.example.calculation.databinding.FragmentDepositBinding
import com.example.calculation.data.local.AppDatabase
import com.example.calculation.data.repository.DepositRepositoryImpl
import com.example.calculation.viewmodel.DepositViewModel
import com.example.domain.models.DepositCalculation

class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DepositViewModel
    private lateinit var adapter: DepositAdapter
    private var userId: Long = -1L

    companion object {
        fun newInstance(userId: Long): DepositFragment {
            val fragment = DepositFragment()
            val args = Bundle()
            args.putLong("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        val repository = DepositRepositoryImpl(database.depositDao())

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DepositViewModel(repository, userId) as T
            }
        }).get(DepositViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadCalculations()
    }

    private fun setupRecyclerView() {
        adapter = DepositAdapter(
            onItemClick = { calculation ->
                showCalculationDetails(calculation)
            },
            onDeleteClick = { calculation ->
                viewModel.deleteCalculation(calculation)
            }
        )
        binding.recyclerViewCalculations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCalculations.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.calculations.observe(viewLifecycleOwner) { calculations ->
            if (calculations != null) {
                adapter.submitList(calculations)
                if (calculations.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.recyclerViewCalculations.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.recyclerViewCalculations.visibility = View.VISIBLE
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    private fun showCalculationDetails(calculation: DepositCalculation) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Детали расчёта")
            .setMessage("""
                Начальная сумма: ${String.format("%.2f", calculation.initialAmount)} ₽
                Период: ${calculation.periodMonths} месяцев
                Ставка: ${String.format("%.1f", calculation.interestRate)}%
                Ежемесячный взнос: ${calculation.monthlyTopUp?.let { String.format("%.2f", it) } ?: "0"} ₽
                Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽
                Начислено процентов: ${String.format("%.2f", calculation.interestEarned)} ₽
            """.trimIndent())
            .setPositiveButton("Закрыть", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}