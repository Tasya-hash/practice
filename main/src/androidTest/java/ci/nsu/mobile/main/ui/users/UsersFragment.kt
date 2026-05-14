package ci.nsu.mobile.main.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.databinding.FragmentUsersBinding
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.shared.ViewModelFactory
import androidx.activity.viewModels
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ViewModelFactory
    private val usersViewModel: UsersViewModel by lazy {
        viewModelFactory.create(UsersViewModel::class.java)
    }

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)

        val serviceLocator = ServiceLocator.getInstance(requireContext())
        viewModelFactory = ViewModelFactory(serviceLocator)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        usersViewModel.loadUsers()
    }

    private fun setupRecyclerView() {
        usersAdapter = UsersAdapter { user ->
            showUserDetailsDialog(user)
        }

        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            usersViewModel.loadUsers()
            lifecycleScope.launch {
                delay(1000)
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun observeViewModel() {
        usersViewModel.users.observe(viewLifecycleOwner) { users ->
            usersAdapter.submitList(users)
            binding.tvEmpty.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
        }

        usersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        usersViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                usersViewModel.error.postValue(null)
            }
        }
    }

    private fun showUserDetailsDialog(user: ci.nsu.mobile.main.data.models.User) {
        AlertDialog.Builder(requireContext())
            .setTitle("Информация о пользователе")
            .setMessage("""
                ID: ${user.id}
                Имя пользователя: ${user.username}
                Email: ${user.email}
                Имя: ${user.firstName ?: "Не указано"}
                Фамилия: ${user.lastName ?: "Не указано"}
            """.trimIndent())
            .setPositiveButton("Закрыть", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}