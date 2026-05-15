package com.example.myapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp.databinding.ActivityMainBinding
import com.example.domain.managers.AuthManager
import com.example.domain.navigation.AuthNavigator
import com.example.domain.navigation.CalculationsNavigator
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authManager: AuthManager by inject()
    private val authNavigator: AuthNavigator by inject()
    private val calculationsNavigator: CalculationsNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthState()
        setupBottomNavigation()
    }

    private fun checkAuthState() {
        lifecycleScope.launch {
            authManager.observeAuthState().collect { state ->
                when (state) {
                    is com.example.domain.models.AuthState.Unauthenticated -> {
                        authNavigator.openAuthFlow(this@MainActivity, AUTH_REQUEST_CODE)
                        finish()
                    }
                    is com.example.domain.models.AuthState.Authenticated -> {
                        // Пользователь авторизован, показываем основной экран
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        val userId = authManager.getUserId() ?: return

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_new_calculation -> {
                    calculationsNavigator.navigateToNewCalculation(this, userId)
                    true
                }
                R.id.nav_my_calculations -> {
                    calculationsNavigator.navigateToMyCalculations(this, userId)
                    true
                }
                R.id.nav_logout -> {
                    lifecycleScope.launch {
                        authManager.logout()
                    }
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 1001
    }
}