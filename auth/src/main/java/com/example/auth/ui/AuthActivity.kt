package com.example.auth.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.auth.databinding.ActivityAuthBinding
import com.example.auth.navigation.AuthNavigatorImpl
import com.example.auth.viewmodel.AuthViewModel
import com.example.domain.interfaces.AuthNavigator

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var authNavigator: AuthNavigator
    private lateinit var authViewModel: AuthViewModel

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authNavigator = AuthNavigatorImpl(this)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.init(this)

        setupUI()
        observeViewModel()

        isLoginMode = intent.getStringExtra("mode") != "register"
        updateUIMode()

        if (authViewModel.isLoggedIn()) {
            authNavigator.navigateToMainAfterAuth()
            finish()
        }
    }

    private fun setupUI() {
        binding.btnToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUIMode()
        }

        binding.btnSubmit.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegistration()
            }
        }
    }

    private fun updateUIMode() {
        if (isLoginMode) {
            binding.btnToggleMode.text = "Нет аккаунта? Зарегистрироваться"
            binding.btnSubmit.text = "Войти"
            binding.titleText.text = "Вход в систему"
            binding.etUsername.visibility = View.GONE
        } else {
            binding.btnToggleMode.text = "Уже есть аккаунт? Войти"
            binding.btnSubmit.text = "Зарегистрироваться"
            binding.titleText.text = "Регистрация"
            binding.etUsername.visibility = View.VISIBLE
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!validateInputs()) return

        authViewModel.login(email, password)
    }

    private fun performRegistration() {
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!validateInputs()) return

        authViewModel.register(email, username, password)
    }

    private fun validateInputs(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "Введите email"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Введите корректный email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Введите пароль"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Пароль должен содержать минимум 6 символов"
            return false
        }

        if (!isLoginMode) {
            val username = binding.etUsername.text.toString().trim()
            if (username.isEmpty()) {
                binding.etUsername.error = "Введите имя пользователя"
                return false
            }
            if (username.length < 3) {
                binding.etUsername.error = "Имя пользователя должно содержать минимум 3 символа"
                return false
            }
        }

        return true
    }

    private fun observeViewModel() {
        authViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSubmit.isEnabled = !isLoading
            binding.btnToggleMode.isEnabled = !isLoading
        }

        authViewModel.loginResult.observe(this) { result ->
            result.fold(
                onSuccess = { user ->
                    Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                    authNavigator.navigateToMainAfterAuth()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка входа: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }

        authViewModel.registerResult.observe(this) { result ->
            result.fold(
                onSuccess = { user ->
                    Toast.makeText(this, "Регистрация выполнена успешно", Toast.LENGTH_SHORT).show()
                    authNavigator.navigateToMainAfterAuth()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка регистрации: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}