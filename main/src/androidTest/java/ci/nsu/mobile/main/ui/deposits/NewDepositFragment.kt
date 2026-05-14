package ci.nsu.mobile.main.ui.deposits
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.databinding.FragmentNewDepositBinding
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.shared.ViewModelFactory
import kotlinx.coroutines.launch

class NewDepositFragment : Fragment() {

    private var _binding: FragmentNewDepositBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactory
    private val depositViewModel: DepositViewModel by lazy {
        viewModelFactory.create(DepositViewModel::class.java)
    }

    private var isStepOneValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewDepositBinding.inflate(inflater, container, false)

        val serviceLocator = ServiceLocator.getInstance(requireContext())
        viewModelFactory = ViewModelFactory(serviceLocator)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStepButtons()
        setupCalculateButton()
        observeViewModel()

        // Показываем первый шаг
        showStepOne()
    }

    private fun setupStepButtons() {
        binding.btnNextStep.setOnClickListener {
            if (validateStepOne()) {
                performCalculation()
                showStepTwo()
            }
        }

        binding.btnBackStep.setOnClickListener {
            showStepOne()
        }
    }

    private fun setupCalculateButton() {
        binding.btnSave.setOnClickListener {
            saveCalculation()
        }

        binding.btnRecalculate.setOnClickListener {
            performCalculation()
            Toast.makeText(requireContext(), "Расчёт обновлён", Toast.LENGTH_SHORT).show()
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

        depositViewModel.calculateDeposit(initialAmount, periodMonths, interestRate, monthlyTopUp)
    }

    private fun saveCalculation() {
        if (!isStepOneValid) {
            Toast.makeText(requireContext(), "Сначала заполните параметры вклада", Toast.LENGTH_SHORT).show()
            showStepOne()
            return
        }

        val initialAmount = binding.etInitialAmount.text.toString().toDouble()
        val periodMonths = binding.etPeriodMonths.text.toString().toInt()
        val interestRate = binding.etInterestRate.text.toString().toDouble()
        val monthlyTopUp = if (binding.cbMonthlyTopUp.isChecked) {
            val topUpText = binding.etMonthlyTopUp.text.toString()
            if (topUpText.isNotEmpty()) topUpText.toDouble() else null
        } else {
            null
        }

        depositViewModel.saveCurrentCalculation(initialAmount, periodMonths, interestRate, monthlyTopUp)
    }

    private fun observeViewModel() {
        depositViewModel.calculationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                binding.tvFinalAmount.text = String.format("%.2f ₽", it.finalAmount)
                binding.tvInterestEarned.text = String.format("%.2f ₽", it.interestEarned)
            }
        }

        depositViewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                depositViewModel.clearMessage()

                // Если расчёт сохранён успешно, очищаем форму
                if (it.contains("сохранён")) {
                    clearForm()
                }
            }
        }
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
        showStepOne()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}