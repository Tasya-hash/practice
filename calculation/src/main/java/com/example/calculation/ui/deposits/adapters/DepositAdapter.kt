package com.example.calculation.ui.deposits.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculation.databinding.ItemDepositCalculationBinding
import com.example.domain.DepositCalculation
import java.text.SimpleDateFormat
import java.util.*

class DepositAdapter(
    private val onItemClick: (DepositCalculation) -> Unit,
    private val onDeleteClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<DepositAdapter.DepositViewHolder>() {

    private var calculations = listOf<DepositCalculation>()

    fun submitList(newList: List<DepositCalculation>) {
        calculations = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositViewHolder {
        val binding = ItemDepositCalculationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DepositViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepositViewHolder, position: Int) {
        holder.bind(calculations[position])
    }

    override fun getItemCount(): Int = calculations.size

    inner class DepositViewHolder(
        private val binding: ItemDepositCalculationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: DepositCalculation) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(calculation.calculationDate))

            binding.tvInitialAmount.text = String.format("%.2f ₽", calculation.initialAmount)
            binding.tvFinalAmount.text = String.format("%.2f ₽", calculation.finalAmount)
            binding.tvPeriod.text = "${calculation.periodMonths} мес."
            binding.tvInterestRate.text = String.format("%.1f%%", calculation.interestRate)
            binding.tvDate.text = formattedDate

            binding.root.setOnClickListener {
                onItemClick(calculation)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(calculation)
            }
        }
    }
}