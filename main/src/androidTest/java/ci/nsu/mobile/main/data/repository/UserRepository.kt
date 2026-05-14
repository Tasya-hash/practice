package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.models.User
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserById(id: Long): Result<User>
}