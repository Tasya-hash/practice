package com.example.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager2.widget.ViewPager2
import com.example.auth.AuthActivity
import com.example.auth.AuthManagerImpl
import com.example.main.adapters.MainPagerAdapter
import com.example.main.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: AuthManagerImpl

    private val authReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "AUTH_SUCCESS") {
                updateUIAfterAuth()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManagerImpl(this)

        checkAuthAndSetup()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            authReceiver,
            IntentFilter("AUTH_SUCCESS")
        )
    }

    private fun checkAuthAndSetup() {
        if (authManager.isLoggedIn()) {
            setupUI()
        } else {
            navigateToAuth()
        }
    }

    private fun setupUI() {
        val userId = authManager.getCurrentUserId()
        val pagerAdapter = MainPagerAdapter(this, userId)
        binding.viewPager.adapter = pagerAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true

                when (position) {
                    0 -> supportActionBar?.title = "Пользователи"
                    1 -> supportActionBar?.title = "Мои расчёты"
                    2 -> supportActionBar?.title = "Новый расчёт"
                }
            }
        })

        setupBottomNavigation()
        supportActionBar?.title = "Расчёт вкладов"
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_users -> {
                    binding.viewPager.currentItem = 0
                    true
                }
                R.id.navigation_my_calculations -> {
                    binding.viewPager.currentItem = 1
                    true
                }
                R.id.navigation_new_calculation -> {
                    binding.viewPager.currentItem = 2
                    true
                }
                else -> false
            }
        }
    }

    private fun updateUIAfterAuth() {
        setupUI()
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun logout() {
        authManager.logout()
        Toast.makeText(this, "Вы вышли из системы", Toast.LENGTH_SHORT).show()
        navigateToAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(authReceiver)
    }
}