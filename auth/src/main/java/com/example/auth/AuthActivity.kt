package com.example.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.auth.databinding.ActivityAuthBinding
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
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

        if (validateInputs()) {
            authViewModel.login(email, password)
        }
    }

    private fun performRegistration() {
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (validateInputs()) {
            authViewModel.register(email, username, password)
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.etEmail.error = "Введите email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Введите пароль"
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "Пароль должен быть минимум 6 символов"
            return false
        }

        if (!isLoginMode) {
            val username = binding.etUsername.text.toString().trim()
            if (username.isEmpty()) {
                binding.etUsername.error = "Введите имя пользователя"
                return false
            }
            if (username.length < 3) {
                binding.etUsername.error = "Имя пользователя минимум 3 символа"
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
                onSuccess = {
                    Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }

        authViewModel.registerResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(this, "Регистрация выполнена", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}