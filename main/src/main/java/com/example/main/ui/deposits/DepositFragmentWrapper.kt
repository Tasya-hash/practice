package com.example.main.ui.deposits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calculation.ui.deposits.fragments.DepositListFragment
class DepositFragmentWrapper : Fragment() {

    private var userId: Long = 0

    companion object {
        fun newInstance(userId: Long): DepositFragmentWrapper {
            val fragment = DepositFragmentWrapper()
            val args = Bundle()
            args.putLong("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getLong("userId", 0) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Просто возвращаем view из вложенного фрагмента
        val wrappedFragment = DepositListFragment.newInstance(userId)
        return wrappedFragment.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wrappedFragment = DepositListFragment.newInstance(userId)
        wrappedFragment.onViewCreated(view, savedInstanceState)
    }
}