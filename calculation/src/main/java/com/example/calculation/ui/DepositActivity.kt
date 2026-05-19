package com.example.calculation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculation.databinding.ActivityDepositBinding

class DepositActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepositBinding
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1L)
        val mode = intent.getStringExtra("mode") ?: "list"

        setupBottomNavigation(mode)

        if (savedInstanceState == null) {
            loadFragmentForMode(mode)
        }
    }

    private fun setupBottomNavigation(mode: String) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.calculation.R.id.navigation_my_calculations -> {
                    loadFragment(DepositFragment.newInstance(userId))
                    true
                }
                com.example.calculation.R.id.navigation_new_calculation -> {
                    loadFragment(NewDepositFragment.newInstance(userId))
                    true
                }
                else -> false
            }
        }

        if (mode != "list") {
            binding.bottomNavigation.visibility = android.view.View.GONE
        }
    }

    private fun loadFragmentForMode(mode: String) {
        val fragment = when (mode) {
            "new" -> NewDepositFragment.newInstance(userId)
            else -> DepositFragment.newInstance(userId)
        }
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(com.example.calculation.R.id.fragmentContainer, fragment)
            .commit()
    }
}