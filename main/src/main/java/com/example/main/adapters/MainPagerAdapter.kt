package com.example.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.main.ui.deposits.DepositFragmentWrapper
import com.example.main.ui.deposits.NewDepositFragmentWrapper
import com.example.main.ui.users.UsersFragment

class MainPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val userId: Long
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UsersFragment.newInstance()
            1 -> DepositFragmentWrapper.newInstance(userId)
            2 -> NewDepositFragmentWrapper.newInstance(userId)
            else -> UsersFragment.newInstance()
        }
    }
}