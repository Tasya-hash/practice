package ci.nsu.mobile.main.ui.main
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.ActivityMainBinding
import ci.nsu.mobile.main.ui.auth.AuthActivity
import ci.nsu.mobile.main.ui.shared.MainPagerAdapter
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.auth.AuthViewModel
import ci.nsu.mobile.main.ui.shared.ViewModelFactory
import androidx.activity.viewModels
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val authViewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceLocator = ServiceLocator.getInstance(this)
        viewModelFactory = ViewModelFactory(serviceLocator)

        setupViewPager()
        setupBottomNavigation()

        supportActionBar?.title = "Расчёт вкладов"
    }

    private fun setupViewPager() {
        val pagerAdapter = MainPagerAdapter(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        authViewModel.logout()
        Toast.makeText(this, "Вы вышли из системы", Toast.LENGTH_SHORT).show()
        navigateToAuth()
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}