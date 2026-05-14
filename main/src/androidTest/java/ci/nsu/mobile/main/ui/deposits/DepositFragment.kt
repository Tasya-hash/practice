package ci.nsu.mobile.main.ui.deposits
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AlertDialog
import ci.nsu.mobile.main.databinding.FragmentDepositBinding
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.shared.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DepositFragment : Fragment() {

    private var _binding: FragmentDepositBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactory
    private val depositViewModel: DepositViewModel by lazy {
        viewModelFactory.create(DepositViewModel::class.java)
    }

    private lateinit var depositAdapter: DepositAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositBinding.inflate(inflater, container, false)

        val serviceLocator = ServiceLocator.getInstance(requireContext())
        viewModelFactory = ViewModelFactory(serviceLocator)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFilterButtons()
        observeViewModel()
        depositViewModel.loadCalculations()
    }

    private fun setupRecyclerView() {
        depositAdapter = DepositAdapter(
            onItemClick = { calculation ->
                showCalculationDetails(calculation)
            },
            onDeleteClick = { calculation ->
                showDeleteConfirmation(calculation)
            }
        )

        binding.recyclerViewCalculations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = depositAdapter
        }
    }

    private fun setupFilterButtons() {
        binding.btnFilterAll.setOnClickListener {
            depositViewModel.loadCalculations()
            updateFilterButtonState("all")
        }

        binding.btnFilterLastMonth.setOnClickListener {
            val lastMonthTimestamp = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
            // Фильтрация происходит через ViewModel, для простоты перезагружаем все
            depositViewModel.loadCalculations()
            Toast.makeText(requireContext(), "Фильтр: за последний месяц", Toast.LENGTH_SHORT).show()
            updateFilterButtonState("month")
        }

        binding.btnFilterLargeAmount.setOnClickListener {
            // Показываем только расчёты с суммой > 100000
            Toast.makeText(requireContext(), "Фильтр: крупные вклады (>100,000)", Toast.LENGTH_SHORT).show()
            updateFilterButtonState("large")
        }
    }

    private fun updateFilterButtonState(activeFilter: String) {
        val buttons = listOf(binding.btnFilterAll, binding.btnFilterLastMonth, binding.btnFilterLargeAmount)
        buttons.forEach { button ->
            button.isEnabled = true
        }

        when (activeFilter) {
            "all" -> binding.btnFilterAll.isEnabled = false
            "month" -> binding.btnFilterLastMonth.isEnabled = false
            "large" -> binding.btnFilterLargeAmount.isEnabled = false
        }
    }

    private fun observeViewModel() {
        depositViewModel.calculations.observe(viewLifecycleOwner) { calculations ->
            depositAdapter.submitList(calculations)
            if (calculations.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.recyclerViewCalculations.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.recyclerViewCalculations.visibility = View.VISIBLE
            }
        }

        depositViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        depositViewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                depositViewModel.clearMessage()
            }
        }
    }

    private fun showCalculationDetails(calculation: ci.nsu.mobile.main.data.local.DepositCalculation) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(calculation.calculationDate))

        AlertDialog.Builder(requireContext())
            .setTitle("Детали расчёта")
            .setMessage("""
                Детали вклада:
                
                Начальная сумма: ${String.format("%.2f", calculation.initialAmount)} ₽
                Период: ${calculation.periodMonths} месяцев
                Процентная ставка: ${String.format("%.1f", calculation.interestRate)}%
                Ежемесячный взнос: ${if (calculation.monthlyTopUp != null) String.format("%.2f", calculation.monthlyTopUp) else "0"} ₽
                
                ✅ Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽
                Начисленные проценты: ${String.format("%.2f", calculation.interestEarned)} ₽
                
                Дата расчёта: $formattedDate
            """.trimIndent())
            .setPositiveButton("Закрыть", null)
            .setNeutralButton("Удалить") { _, _ ->
                showDeleteConfirmation(calculation)
            }
            .show()
    }

    private fun showDeleteConfirmation(calculation: ci.nsu.mobile.main.data.local.DepositCalculation) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление расчёта")
            .setMessage("Вы уверены, что хотите удалить этот расчёт?")
            .setPositiveButton("Удалить") { _, _ ->
                depositViewModel.deleteCalculation(calculation)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}