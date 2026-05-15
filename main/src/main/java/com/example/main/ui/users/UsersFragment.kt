package com.example.main.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.main.databinding.FragmentUsersBinding
import com.example.main.data.models.User
import com.example.main.data.remote.RetrofitClient
import com.example.main.utils.TokenManager
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var usersAdapter: UsersAdapter
    private lateinit var tokenManager: TokenManager

    companion object {
        fun newInstance(): UsersFragment {
            return UsersFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())
        setupRecyclerView()
        setupSwipeRefresh()
        loadUsers()
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
            loadUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val apiService = RetrofitClient.getApiService(tokenManager)
                val response = apiService.getUsers()
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        usersAdapter.submitList(users)
                        binding.tvEmpty.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
                    }
                } else {
                    Toast.makeText(requireContext(), "Ошибка загрузки пользователей", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showUserDetailsDialog(user: User) {
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