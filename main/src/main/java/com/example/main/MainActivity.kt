package com.example.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculation.ui.DepositFragment
import com.example.calculation.ui.NewDepositFragment
import com.example.main.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var userId: Long = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_my_calculations -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, DepositFragment.newInstance(userId))
                        .commit()
                    true
                }
                R.id.navigation_new_calculation -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, NewDepositFragment.newInstance(userId))
                        .commit()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_my_calculations
    }
}