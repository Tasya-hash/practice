package ci.nsu.mobile.main.di
import android.content.Context
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.remote.RetrofitClient
import ci.nsu.mobile.main.data.repository.*
import ci.nsu.mobile.main.utils.TokenManager
import ci.nsu.mobile.main.ui.auth.AuthViewModel
import ci.nsu.mobile.main.ui.deposits.DepositViewModel
import ci.nsu.mobile.main.ui.users.UsersViewModel

class ServiceLocator private constructor(private val context: Context) {

    private val tokenManager: TokenManager by lazy {
        TokenManager(context.applicationContext)
    }

    private val apiService by lazy {
        RetrofitClient.getApiService(tokenManager)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService, tokenManager)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(apiService)
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context.applicationContext)
    }

    private val depositDao by lazy {
        database.depositDao()
    }

    val depositRepository: DepositRepository by lazy {
        DepositRepositoryImpl(depositDao)
    }

    fun provideAuthViewModel(): AuthViewModel {
        return AuthViewModel(authRepository, tokenManager)
    }

    fun provideUsersViewModel(): UsersViewModel {
        return UsersViewModel(userRepository)
    }

    fun provideDepositViewModel(): DepositViewModel {
        return DepositViewModel(depositRepository, tokenManager)
    }

    companion object {
        @Volatile
        private var INSTANCE: ServiceLocator? = null

        fun getInstance(context: Context): ServiceLocator {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ServiceLocator(context).also { INSTANCE = it }
            }
        }
    }
}
