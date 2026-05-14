package ci.nsu.mobile.main.ui.shared

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ci.nsu.mobile.main.ui.deposits.DepositFragment
import ci.nsu.mobile.main.ui.deposits.NewDepositFragment
import ci.nsu.mobile.main.ui.users.UsersFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UsersFragment()
            1 -> DepositFragment()
            2 -> NewDepositFragment()
            else -> UsersFragment()
        }
    }
}