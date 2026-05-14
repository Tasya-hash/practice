package ci.nsu.mobile.main.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.auth.AuthViewModel
import ci.nsu.mobile.main.ui.deposits.DepositViewModel
import ci.nsu.mobile.main.ui.users.UsersViewModel

class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                serviceLocator.provideAuthViewModel() as T
            }
            modelClass.isAssignableFrom(UsersViewModel::class.java) -> {
                serviceLocator.provideUsersViewModel() as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                serviceLocator.provideDepositViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}