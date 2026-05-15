package com.example.calculation.ui.deposits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.calculation.databinding.ActivityDepositBinding

class DepositActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepositBinding
    private lateinit var pagerAdapter: DepositPagerAdapter

    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", 0)
        val mode = intent.getStringExtra("mode") ?: "list"

        setupViewPager(mode)
        setupBottomNavigation()
    }

    private fun setupViewPager(initialMode: String) {
        pagerAdapter = DepositPagerAdapter(this, userId)
        binding.viewPager.adapter = pagerAdapter

        val initialPosition = when (initialMode) {
            "new" -> 1
            "list" -> 0
            else -> 0
        }
        binding.viewPager.currentItem = initialPosition

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true
                updateTitle(position)
            }
        })
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_my_calculations -> {
                    binding.viewPager.currentItem = 0
                    true
                }
                R.id.navigation_new_calculation -> {
                    binding.viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }
    }

    private fun updateTitle(position: Int) {
        supportActionBar?.title = when (position) {
            0 -> "Мои расчёты"
            1 -> "Новый расчёт"
            else -> "Расчёты"
        }
    }
}