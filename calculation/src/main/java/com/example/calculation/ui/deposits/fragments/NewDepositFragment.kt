package com.example.calculation.ui.deposits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calculation.databinding.FragmentNewDepositBinding
import com.example.calculation.data.local.DepositDatabase
import com.example.calculation.ui.deposits.viewmodels.NewDepositViewModel

class NewDepositFragment : Fragment() {

    private var _binding: FragmentNewDepositBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewDepositViewModel
    private var userId: Long = 0
    private var isStepOneValid = false

    companion object {
        fun newInstance(userId: Long): NewDepositFragment {
            val fragment = NewDepositFragment()
            val args = Bundle()
            args.putLong("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId", 0) ?: 0
        viewModel = NewDepositViewModel(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewDepositBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = DepositDatabase.getDatabase(requireContext())
        viewModel.init(database)

        setupListeners()
        observeViewModel()
        showStepOne()
    }

    private fun setupListeners() {
        binding.cbMonthlyTopUp.setOnCheckedChangeListener { _, isChecked ->
            binding.etMonthlyTopUp.isEnabled = isChecked
            binding.monthlyTopUpLayout.isEnabled = isChecked
        }

        binding.btnNextStep.setOnClickListener {
            if (validateStepOne()) {
                performCalculation()
                showStepTwo()
            }
        }

        binding.btnBackStep.setOnClickListener {
            showStepOne()
        }

        binding.btnSave.setOnClickListener {
            saveCalculation()
        }

        binding.btnRecalculate.setOnClickListener {
            performCalculation()
            Toast.makeText(requireContext(), "Расчёт обновлён", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.calculationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                binding.tvFinalAmount.text = String.format("%.2f ₽", it.finalAmount)
                binding.tvInterestEarned.text = String.format("%.2f ₽", it.interestEarned)
            }
        }

        viewModel.isCalculating.observe(viewLifecycleOwner) { isCalculating ->
            binding.progressBar.visibility = if (isCalculating) View.VISIBLE else View.GONE
        }

        viewModel.isSaved.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                clearForm()
                showStepOne()
                viewModel.resetSaved()
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }

    private fun validateStepOne(): Boolean {
        val initialAmount = binding.etInitialAmount.text.toString()
        val periodMonths = binding.etPeriodMonths.text.toString()
        val interestRate = binding.etInterestRate.text.toString()

        if (initialAmount.isEmpty()) {
            binding.etInitialAmount.error = "Введите начальную сумму"
            return false
        }

        val initialAmountValue = initialAmount.toDoubleOrNull()
        if (initialAmountValue == null || initialAmountValue <= 0) {
            binding.etInitialAmount.error = "Введите корректную сумму"
            return false
        }

        if (periodMonths.isEmpty()) {
            binding.etPeriodMonths.error = "Введите период"
            return false
        }

        val periodValue = periodMonths.toIntOrNull()
        if (periodValue == null || periodValue <= 0 || periodValue > 600) {
            binding.etPeriodMonths.error = "Введите период от 1 до 600 месяцев"
            return false
        }

        if (interestRate.isEmpty()) {
            binding.etInterestRate.error = "Введите процентную ставку"
            return false
        }

        val rateValue = interestRate.toDoubleOrNull()
        if (rateValue == null || rateValue <= 0 || rateValue > 100) {
            binding.etInterestRate.error = "Введите ставку от 0 до 100%"
            return false
        }

        isStepOneValid = true
        return true
    }

    private fun performCalculation() {
        val initialAmount = binding.etInitialAmount.text.toString().toDouble()
        val periodMonths = binding.etPeriodMonths.text.toString().toInt()
        val interestRate = binding.etInterestRate.text.toString().toDouble()
        val monthlyTopUp = if (binding.cbMonthlyTopUp.isChecked) {
            val topUpText = binding.etMonthlyTopUp.text.toString()
            if (topUpText.isNotEmpty()) topUpText.toDouble() else null
        } else {
            null
        }

        viewModel.calculateDeposit(initialAmount, periodMonths, interestRate, monthlyTopUp)
    }

    private fun saveCalculation() {
        if (!isStepOneValid) {
            Toast.makeText(requireContext(), "Сначала заполните параметры вклада", Toast.LENGTH_SHORT).show()
            showStepOne()
            return
        }
        viewModel.saveCalculation()
    }

    private fun showStepOne() {
        binding.stepOneLayout.visibility = View.VISIBLE
        binding.stepTwoLayout.visibility = View.GONE
        binding.btnNextStep.visibility = View.VISIBLE
        binding.btnBackStep.visibility = View.GONE
    }

    private fun showStepTwo() {
        binding.stepOneLayout.visibility = View.GONE
        binding.stepTwoLayout.visibility = View.VISIBLE
        binding.btnNextStep.visibility = View.GONE
        binding.btnBackStep.visibility = View.VISIBLE
    }

    private fun clearForm() {
        binding.etInitialAmount.text?.clear()
        binding.etPeriodMonths.text?.clear()
        binding.etInterestRate.text?.clear()
        binding.etMonthlyTopUp.text?.clear()
        binding.cbMonthlyTopUp.isChecked = false
        binding.tvFinalAmount.text = "0.00 ₽"
        binding.tvInterestEarned.text = "0.00 ₽"
        isStepOneValid = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}