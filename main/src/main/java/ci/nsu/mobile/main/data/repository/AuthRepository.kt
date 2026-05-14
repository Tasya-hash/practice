package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.models.AuthRequest
import ci.nsu.mobile.main.data.models.AuthResponse
import ci.nsu.mobile.main.data.models.RegisterRequest
import retrofit2.Response

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, username: String, password: String): Result<AuthResponse>
}