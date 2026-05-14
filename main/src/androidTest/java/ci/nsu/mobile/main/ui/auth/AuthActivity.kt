package ci.nsu.mobile.main.ui.auth
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.databinding.ActivityAuthBinding
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.main.MainActivity
import ci.nsu.mobile.main.ui.shared.ViewModelFactory
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceLocator = ServiceLocator.getInstance(this)
        viewModelFactory = ViewModelFactory(serviceLocator)

        setupUI()
        observeViewModel()

        // Проверяем, авторизован ли пользователь
        if (authViewModel.isLoggedIn()) {
            navigateToMain()
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
            binding.usernameInputLayout.visibility = View.GONE
        } else {
            binding.btnToggleMode.text = "Уже есть аккаунт? Войти"
            binding.btnSubmit.text = "Зарегистрироваться"
            binding.titleText.text = "Регистрация"
            binding.usernameInputLayout.visibility = View.VISIBLE
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
                onSuccess = {
                    Toast.makeText(this, "Вход выполнен успешно", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка входа: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }

        authViewModel.registerResult.observe(this) { result ->
            result.fold(
                onSuccess = {
                    Toast.makeText(this, "Регистрация выполнена успешно", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                },
                onFailure = { exception ->
                    Toast.makeText(this, "Ошибка регистрации: ${exception.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}