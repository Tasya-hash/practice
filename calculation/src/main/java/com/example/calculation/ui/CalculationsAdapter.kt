package com.example.calculation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculation.databinding.ItemCalculationBinding
import com.example.domain.models.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Locale

class CalculationsAdapter(
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<CalculationsAdapter.CalculationViewHolder>() {

    private var calculations = listOf<DepositCalculation>()

    fun submitList(newList: List<DepositCalculation>) {
        calculations = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculationViewHolder {
        val binding = ItemCalculationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalculationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalculationViewHolder, position: Int) {
        holder.bind(calculations[position])
    }

    override fun getItemCount(): Int = calculations.size

    inner class CalculationViewHolder(
        private val binding: ItemCalculationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: DepositCalculation) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            binding.tvAmount.text = String.format("Сумма: %.2f ₽", calculation.amount)
            binding.tvResult.text = String.format("Итог: %.2f ₽", calculation.resultAmount)
            binding.tvDate.text = dateFormat.format(calculation.dateCreated)
            binding.tvTerm.text = String.format("Срок: %d мес.", calculation.termMonths)

            binding.root.setOnClickListener {
                onItemClick(calculation)
            }
        }
    }
}