package com.example.myapp.practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.practice.databinding.ActivityMainBinding
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
        setupButtons()
    }

    private fun checkAuthState() {
        lifecycleScope.launch {
            authManager.observeAuthState().collect { state ->
                when (state) {
                    is com.example.domain.models.AuthState.Unauthenticated -> {
                        authNavigator.openAuthFlow(this@MainActivity, AUTH_REQUEST_CODE)
                        finish()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupButtons() {
        val userId = authManager.getUserId() ?: 1L

        binding.btnNewCalculation.setOnClickListener {
            calculationsNavigator.navigateToNewCalculation(this, userId)
        }

        binding.btnMyCalculations.setOnClickListener {
            calculationsNavigator.navigateToMyCalculations(this, userId)
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                authManager.logout()
            }
        }
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 1001
    }
}