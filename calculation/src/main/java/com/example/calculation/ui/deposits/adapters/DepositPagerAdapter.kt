package com.example.calculation.ui.deposits.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calculation.ui.deposits.fragments.DepositListFragment
import com.example.calculation.ui.deposits.fragments.NewDepositFragment
class DepositPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val userId: Long
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DepositListFragment.newInstance(userId)
            1 -> NewDepositFragment.newInstance(userId)
            else -> DepositListFragment.newInstance(userId)
        }
    }
}