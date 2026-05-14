package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException
import ci.nsu.mobile.main.data.models.User

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { users ->
                    Result.success(users)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to load users: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: Long): Result<User> {
        return try {
            val response = apiService.getUserById(id)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    Result.success(user)
                } ?: Result.failure(Exception("User not found"))
            } else {
                Result.failure(Exception("Failed to load user: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: HttpException) {
            Result.failure(Exception("HTTP error: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}