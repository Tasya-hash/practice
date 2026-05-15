package com.example.calculation.ui.deposits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculation.databinding.FragmentDepositListBinding
import com.example.calculation.ui.deposits.adapters.DepositAdapter
import com.example.calculation.ui.deposits.viewmodels.DepositListViewModel
import com.example.domain.DepositCalculation

class DepositListFragment : Fragment() {

    private var _binding: FragmentDepositListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DepositListViewModel
    private lateinit var adapter: DepositAdapter
    private var userId: Long = 0

    companion object {
        fun newInstance(userId: Long): DepositListFragment {
            val fragment = DepositListFragment()
            val args = Bundle()
            args.putLong("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId", 0) ?: 0
        viewModel = DepositListViewModel(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                viewModel.deleteCalculation(calculation.id)
            }
        )

        binding.recyclerViewCalculations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@DepositListFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.calculations.observe(viewLifecycleOwner) { calculations ->
            adapter.submitList(calculations)
            binding.tvEmpty.visibility = if (calculations.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerViewCalculations.visibility = if (calculations.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    private fun showCalculationDetails(calculation: DepositCalculation) {
        val dateFormat = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
        val formattedDate = dateFormat.format(java.util.Date(calculation.calculationDate))

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Детали расчёта")
            .setMessage("""
                Начальная сумма: ${String.format("%.2f", calculation.initialAmount)} ₽
                Период: ${calculation.periodMonths} месяцев
                Ставка: ${String.format("%.1f", calculation.interestRate)}%
                Ежемесячный взнос: ${if (calculation.monthlyTopUp != null) String.format("%.2f", calculation.monthlyTopUp) else "0"} ₽
                
                Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽
                Начисленные проценты: ${String.format("%.2f", calculation.interestEarned)} ₽
                
                Дата: $formattedDate
            """.trimIndent())
            .setPositiveButton("Закрыть", null)
            .setNeutralButton("Удалить") { _, _ ->
                viewModel.deleteCalculation(calculation.id)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}