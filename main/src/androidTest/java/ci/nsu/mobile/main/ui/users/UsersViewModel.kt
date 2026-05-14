package ci.nsu.mobile.main.ui.users
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.User
import ci.nsu.mobile.main.data.repository.UserRepository
import kotlinx.coroutines.launch

class UsersViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _selectedUser = MutableLiveData<User?>()
    val selectedUser: LiveData<User?> = _selectedUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = userRepository.getUsers()
            result.fold(
                onSuccess = { users ->
                    _users.value = users
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )
            _isLoading.value = false
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = user
    }

    fun clearSelectedUser() {
        _selectedUser.value = null
    }
}
