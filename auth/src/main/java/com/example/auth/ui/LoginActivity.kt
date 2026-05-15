package com.example.auth.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.auth.databinding.ActivityLoginBinding
import com.example.auth.viewmodel.AuthViewModel
import com.example.domain.managers.AuthManager
import com.example.domain.navigation.AuthNavigator
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by inject()
    private val authNavigator: AuthNavigator by inject()
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(username, password)
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            authNavigator.navigateToRegister(this)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            authViewModel.loginResult.collect { result ->
                result.onSuccess { user ->
                    Toast.makeText(this@LoginActivity, "Добро пожаловать, ${user.username}", Toast.LENGTH_SHORT).show()
                    authNavigator.navigateToMain(this@LoginActivity)
                    finish()
                }.onFailure { error ->
                    Toast.makeText(this@LoginActivity, "Ошибка: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}